/**
 * TODO: Edit this
 *
 * User: sam
 * Date: 5/19/13
 * Time: 6:20 PM
 */
package redis

import (
	"testing"
	"strings"
	"fmt"
	"bytes"
	"bufio"
)

func Test_readLong(t *testing.T) {
	result, err := readLong(bufio.NewReader(strings.NewReader("123456789\r\n")))
	if err != nil {
		t.Error("Read error", err)
	} else if 123456789 != result {
		t.Error("Not equal: " + fmt.Sprintf("%d", result))
	}
	result, err = readLong(bufio.NewReader(strings.NewReader("-123456789\r\n")))
	if err != nil {
		t.Error("Read error", err)
	} else if -123456789 != result {
		t.Error("Not equal: " + fmt.Sprintf("%d", result))
	}
}

func Test_readBytes(t *testing.T) {
	result, err := readBytes(strings.NewReader("3\r\nSam\r\n"))
	if err != nil {
		t.Error("Read error", err)
	} else if !bytes.Equal([]byte("Sam"), result) {
		t.Error("Not equal: " + string(result))
	}
}

