LEIN=lein

all:
	@cat Makefile

build:
	$(LEIN) compile

clean:
	$(LEIN) clean

run:
	$(LEIN) run

test:
	$(LEIN) test

b: build
r: run
t: test
