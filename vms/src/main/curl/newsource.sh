#!/usr/bin/env bash
curl -H "Content-Type: application/json" -X POST -d '{"name":"siggraph-2006","description":"for testing purposes","url":"file://sig2006"}' http://zavijava.dia.fi.upm.es:8080/api/rest/sources
