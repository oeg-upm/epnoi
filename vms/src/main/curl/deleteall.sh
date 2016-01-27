#!/usr/bin/env bash
curl -X DELETE "http://zavijava.dia.fi.upm.es:8080/api/0.1/domains"
curl -X DELETE "http://zavijava.dia.fi.upm.es:8080/api/0.1/sources"
curl -X DELETE "http://zavijava.dia.fi.upm.es:8080/api/0.1/documents"
curl -X DELETE "http://zavijava.dia.fi.upm.es:8080/api/0.1/items"
curl -X DELETE "http://zavijava.dia.fi.upm.es:8080/api/0.1/parts"
curl -X DELETE "http://zavijava.dia.fi.upm.es:8080/api/0.1/words"
curl -X DELETE "http://zavijava.dia.fi.upm.es:8080/api/0.1/topics"
