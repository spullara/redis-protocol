/**
 * TODO: Edit this
 *
 * User: sam
 * Date: 5/19/13
 * Time: 5:44 PM
 */
package redis

import "io"
import (
	"errors"
	"fmt"
	"bufio"
)

const (
	StatusMarker    = '+'
	BulkMarker      = '$'
	IntegerMarker   = ':'
	MultiBulkMarker = '*'
	ErrorMarker     = '-'
)

type Reply interface {
}

type SimpleReply struct {
	bytes []byte
}

type StatusReply SimpleReply
type ErrorReply SimpleReply
type BulkReply SimpleReply

type IntegerReply struct {
	integer int64
}

type MultiBulkReply struct {
	replies []Reply
}

func (reply *StatusReply) Status() (status string) {
	return string(reply.bytes)
}

func (reply *ErrorReply) Error() (err string) {
	return string(reply.bytes)
}

func Receive(in io.Reader) (reply Reply, err error) {
	br := bufio.NewReader(in)
	code, err := br.ReadByte()
	if err != nil {
		return nil, err
	}
	switch code {
	case StatusMarker:
		line, _, err := br.ReadLine()
		if err != nil {
			return nil, err
		}
		return &StatusReply {
			bytes: line,
		}, nil
	case ErrorMarker:
		line, _, err := br.ReadLine()
		if err != nil {
			return nil, err
		}
		return &ErrorReply {
			bytes: line,
		}, nil
	case IntegerMarker:
		integer, err := readLong(br)
		if err != nil {
			return nil, err
		}
		return &IntegerReply {
			integer: integer,
		}, nil
	case BulkMarker:
		bytes, err := readBytes(br)
		if err != nil {
			return nil, err
		}
		return &BulkReply {
			bytes: bytes,
		}, nil
	case MultiBulkMarker:
		size, err := readLong(br)
		if err != nil {
			return nil, err
		}
		if size == -1 {
			return &MultiBulkReply {
				replies: nil,
			}, nil
		} else {
			if size < 0 {
				return nil, errors.New("Invalid negative size")
			}
			replies := make([]Reply, size)
			for i := 0; i < int(size); i++ {
				replies[i], err = Receive(br)
				if err != nil {
					return nil, err
				}
			}
			return &MultiBulkReply {
				replies: replies,
			}, nil
		}
		return nil, nil
	default:
		return nil, errors.New("Unexpected character in stream")
	}
	panic("Should not reach here")
}


func readBytes(in io.Reader) (bytes []byte, err error) {
	br := bufio.NewReader(in)
	size, err := readLong(br)
	if err != nil {
		return nil, err
	}
	if size == -1 {
		return nil, nil
	}
	if size < 0 {
		return nil, errors.New("Invalid size: " + fmt.Sprint("%d", size))
	}
	bytes = make([]byte, int(size))
	lin := io.LimitReader(br, size)
	for total := 0; total < int(size); {
		read, err := lin.Read(bytes[total:size])
		if err != nil {
			return nil, err
		}
		total += read
	}
	cr, err := br.ReadByte()
	if err != nil {
		return nil, err
	}
	lf, err := br.ReadByte()
	if err != nil {
		return nil, err
	}
	if cr != 13 || lf != 10 {
		return nil, errors.New("Improper line ending")
	}
	return
}

func readLong(in *bufio.Reader) (result int64, err error) {
	read, err := in.ReadByte()
	if err != nil {
		return -1, err
	}
	var sign int
	if read == '-' {
		read, err = in.ReadByte()
		if err != nil {
			return -1, err
		}
		sign = -1
	} else {
		sign = 1
	}
	var number int64
	for number = 0; err == nil; read, err = in.ReadByte() {
		if read == 13 {
			read, err = in.ReadByte()
			if err != nil {
				return -1, err
			}
			if read == 10 {
				return number*int64(sign), nil
			} else {
				return -1, errors.New("Bad line ending")
			}
		}
		value := read - '0'
		if value >= 0 && value < 10 {
			number *= 10
			number += int64(value)
		} else {
			return -1, errors.New("Invalid digit")
		}
	}
	return -1, err
}

