all: clean
	mkdir build/
	javac -source 1.6 -target 1.6 -deprecation -sourcepath src/main/java -d build/ -implicit:class -g:none src/main/java/thebombzen/tanks/Tanks.java
	jar -cmf jar_manifest.mf Tanks.jar -C build/ thebombzen/


clean: 
	rm -rf build/
	rm -f Tanks.jar

