package redis.netty4;

/*
 * #%L
 * Webcom base server
 * 
 * Module name: com.orange.webcom:webcombase-server
 * Created:     2014-04-07 by Gael Breard
 * %%
 * Copyright (C) 2013 - 2014 Orange Labs
 * %%
 * This software is confidential and proprietary information of Orange.
 * You shall not disclose such Confidential Information and shall use it only in
 * accordance with the terms of the agreement you entered into.
 * Unauthorized copying of this file, via any medium is strictly prohibited.
 * 
 * If you are Orange employee you shall use this software in accordance with
 * the Orange Source Charter (http://opensource.itn.ftgroup/index.php/Orange_Source).
 * #L%
 */

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import io.netty.util.concurrent.Promise;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import redis.embedded.RedisServer;
import redis.netty4.AbstarctReply;
import redis.netty4.BulkReply;
import redis.netty4.IntegerReply;
import redis.netty4.MultiBulkReply;
import redis.netty4.RedisClient;
import redis.netty4.ReplyListener;
import redis.netty4.StatusReply;

public class TestRedis {

	private static final Logger LOG = LoggerFactory.getLogger(TestRedis.class);

	private static final String TESTSTR = "{\"type\":\"candidate\",\"from\":\"foobar@gmail.com, data\":{\"label\":0,\"id\":\"audio\",\"candidate\":\"a=candidate:2084443758 1 tcp 1509957375 192.168.2.4 50030 typ host generation 0\\r\\n\"}}";

	private static final String LONG_STRING = "___"; 

	private static final String M1 = "msg1" + LONG_STRING;
	private static final String M2 = "msg2" + LONG_STRING;
	private static final String M3 = "msg3" + LONG_STRING;
	private static final String M4 = "msg4" + LONG_STRING;
	private static final String M5 = "msg5" + LONG_STRING;
	private static final String M6 = "msg6" + LONG_STRING;
	private static final String M7 = "msg7" + LONG_STRING;
	private static final String M8 = "msg8" + LONG_STRING;
	private static final String M9 = "msg9" + LONG_STRING;
	private static final String M10 = "msg10" + LONG_STRING;
	private static final String M11 = "msg11" + LONG_STRING;

	private static final String P1 = "pubmsg1";
	private static final String P2 = "pubmsg2";
	private static final String P3 = "pubmsg3";
	private static final String P4 = "pubmsg4";
	private static final String P5 = "pubmsg5";
	private static final String P6 = "pubmsg6";
	private static final String P7 = "pubmsg7";
	private static final String P8 = "pubmsg8";
	private static final String P9 = "pubmsg9";

	private static final String REDIS_SERVER_HOST = "127.0.0.1";

	private int redisServerPort;
	
  private RedisServer redisServer;

  @Before
  public void setup() throws Exception {
    redisServer = new RedisServer();
    redisServer.start();
    redisServerPort =  redisServer.getPort();
  }

  @After
  public void tearDown() throws Exception {
    redisServer.stop();
  }

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void simpleTest1() throws Exception {

		EventLoopGroup eventLoopGroup = new NioEventLoopGroup(1);
		RedisClient redisClient = new RedisClient(eventLoopGroup);
		redisClient.connect(REDIS_SERVER_HOST, redisServerPort).sync();

		StatusReply status = redisClient.set("toto", "titi").get();
		assertEquals(StatusReply.OK.data(), status.data());

		BulkReply val = redisClient.get("toto").get();
		assertEquals("titi", val.asUTF8String());

		//AbstarctReply mbr = redisClient.zrange("toto", 0, -1, null).get();

		Promise<String> pString = eventLoopGroup.next().<String> newPromise();
		Promise pInt = pString;

		Promise<Integer> pInt2 = pInt;

		pInt2.setSuccess(new Integer(42));
		Integer totoInt = pInt2.get();
		System.out.println(totoInt);

	}

	@Test
	public void test() throws Exception {
		final Object rdv = new Object();

		GenericFutureListener<Future<IntegerReply>> blockInt = new GenericFutureListener<Future<IntegerReply>>() {
			@Override
			public void operationComplete(Future<IntegerReply> t) throws InterruptedException, ExecutionException {
				System.out.println("fini INT : " + t.get().data());
			}
		};

		GenericFutureListener<Future<IntegerReply>> blockIntEnd = new GenericFutureListener<Future<IntegerReply>>() {
			@Override
			public void operationComplete(Future<IntegerReply> t) throws InterruptedException, ExecutionException {
				System.out.println("fini INT : " + t.get().data());
				synchronized (rdv) {
					rdv.notify();
				}
			}
		};

		GenericFutureListener<Future<MultiBulkReply>> blockMbr = new GenericFutureListener<Future<MultiBulkReply>>() {
			@Override
			public void operationComplete(Future<MultiBulkReply> future) throws Exception {
				System.out.println("fini MBR : " + future.get().data());
			}
		};

		GenericFutureListener<Future<MultiBulkReply>> blockMbrCheck = new GenericFutureListener<Future<MultiBulkReply>>() {
			@Override
			public void operationComplete(Future<MultiBulkReply> t) throws InterruptedException, ExecutionException {
				System.out.println("fini MBR : " + t.get().data());
				assertEquals(M1, t.get().data()[0].toString());
				assertEquals(M8, t.get().data()[7].toString());
				System.out.println("check OK");
			}
		};

		EventLoopGroup eventLoopGroup = new NioEventLoopGroup();
		RedisClient redisClient = new RedisClient(eventLoopGroup);
		redisClient.connect(REDIS_SERVER_HOST, redisServerPort);

		redisClient.flushall();

		redisClient.lrange("k1", 0, -1).addListener(blockMbr);

		redisClient.rpush_("k1", M1).addListener(blockInt);
		redisClient.lrange("k1", 0, -1).addListener(blockMbr);
		redisClient.publish("c1", P1).addListener(blockInt);
		redisClient.lrange("k1", 0, -1).addListener(blockMbr);
		redisClient.rpush_("k1", M2).addListener(blockInt);
		redisClient.publish("c1", P2).addListener(blockInt);
		redisClient.lrange("k1", 0, -1).addListener(blockMbr);
		redisClient.rpush_("k1", M3).addListener(blockInt);
		redisClient.publish("c1", P3).addListener(blockInt);
		redisClient.lrange("k1", 0, -1).addListener(blockMbr);
		redisClient.rpush_("k1", M4).addListener(blockInt);
		redisClient.publish("c1", P4).addListener(blockInt);

		redisClient.rpush_("k1", M5).addListener(blockInt);
		redisClient.lrange("k1", 0, -1).addListener(blockMbr);
		redisClient.publish("c1", P5).addListener(blockInt);
		redisClient.lrange("k1", 0, -1).addListener(blockMbr);
		redisClient.rpush_("k1", M6).addListener(blockInt);
		redisClient.publish("c1", P6).addListener(blockInt);
		redisClient.lrange("k1", 0, -1).addListener(blockMbr);
		redisClient.rpush_("k1", M7).addListener(blockInt);
		redisClient.publish("c1", P7).addListener(blockInt);
		redisClient.lrange("k1", 0, -1).addListener(blockMbr);
		redisClient.rpush_("k1", M8).addListener(blockInt);
		redisClient.publish("c1", P8).addListener(blockInt);

		redisClient.lrange("k1", 0, -1).addListener(blockMbrCheck);

		redisClient.publish("c1", P9).addListener(blockIntEnd);
		System.out.println("wait...");
		synchronized (rdv) {
			rdv.wait();
		}

		System.out.println("end");

	}

	@Test
	public void testPubSub() throws Exception {
		EventLoopGroup eventLoopGroup = new NioEventLoopGroup();
		RedisClient redisClientSub = new RedisClient(eventLoopGroup);
		RedisClient redisClientCmd = new RedisClient(eventLoopGroup);

		redisClientSub.connect(REDIS_SERVER_HOST, redisServerPort);
		redisClientCmd.connect(REDIS_SERVER_HOST, redisServerPort);
		
		final BlockingQueue<String> msgQueue = new LinkedBlockingQueue<>();

		
		ReplyListener replyListener = new ReplyListener() {

			@Override
			public void unsubscribed(byte[] name, int channels) {
				LOG.debug("unsubscribed {},{}", name, channels);
			}
			@Override
			public void subscribed(byte[] name, int channels) {
				LOG.debug("subscribed {},{}", name, channels);
			}
			@Override
			public void punsubscribed(byte[] name, int channels) {
				LOG.debug("punsubscribed {},{}", name, channels);
			}
			@Override
			public void psubscribed(byte[] name, int channels) {
				LOG.debug("psubscribed {},{}", name, channels);
			}
			@Override
			public void pmessage(byte[] pattern, byte[] channel, byte[] message) {
				LOG.debug("pmessage {},{},{}", new String(pattern), new String(channel),new String(message));
				msgQueue.add(new String(message));
			}
			@Override
			public void message(byte[] channel, byte[] message) {
				LOG.debug("message {},{},{}", new String(channel),new String(message));
				msgQueue.add(new String(message));
			}
		};

		redisClientSub.addListener(replyListener);

		boolean res = redisClientSub.psubscribe("ns/a/*").sync().isSuccess();
		assertTrue(res);
		res = redisClientSub.psubscribe("ns/b/*").sync().isSuccess();
		assertTrue(res);

		redisClientCmd.publish("ns/b/toto", "titi").sync().isSuccess();
		assertTrue(res);
		
		String msg = msgQueue.poll(5000, TimeUnit.MILLISECONDS);
		assertEquals("titi", msg);
		
		

		res = redisClientSub.psubscribe("ns/b/bb/*").sync().isSuccess();
		assertTrue(res);

		redisClientCmd.publish("ns/b/bb/toto", "titiNS").sync().isSuccess();
		assertTrue(res);
		

		msg = msgQueue.poll(5000, TimeUnit.MILLISECONDS);
		assertEquals("titiNS", msg);
		

		msg = msgQueue.poll(5000, TimeUnit.MILLISECONDS);
		assertEquals("titiNS", msg);
		
		//Thread.sleep(30000);
		assertTrue(redisClientSub.channel().isActive());
		assertTrue(redisClientCmd.channel().isActive());

		redisClientSub.close().sync();
		redisClientCmd.close().sync();

	}

}
