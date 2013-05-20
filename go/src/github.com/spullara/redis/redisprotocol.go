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
				return number * int64(sign), nil
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

