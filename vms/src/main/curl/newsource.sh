#!/usr/bin/env bash
curl -H "Content-Type: application/json" -X POST -d '{"name":"siggraph","description":"for testing purposes","url":"file://siggraph"}' http://zavijava.dia.fi.upm.es:8080/api/v0.1/sources
