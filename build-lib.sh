#!/bin/sh

# -Xexperimental required for dependent method types
scalac -Xexperimental -d bin/ `find src/ -name "*.scala"`

