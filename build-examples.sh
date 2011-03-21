#!/bin/sh

scalac -d bin/ -cp bin/ `find examples/ -name "*.scala"`
