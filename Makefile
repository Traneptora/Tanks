all: Tanks.jar

Tanks.jar: build/thebombzen/tanks/Tanks.class
	jar -cmf jar_manifest.mf Tanks.jar -C build/ thebombzen/

build/thebombzen/tanks/Tanks.class:
	mkdir -p build/
	javac -source 1.6 -target 1.6 -deprecation -sourcepath src/ -d build/ -implicit:class -g:none src/main/java/thebombzen/tanks/Tanks.java

clean: 
	rm -rf build/
	rm -f Tanks.jar

