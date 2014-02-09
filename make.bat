RMDIR /S /Q build
DEL /S /Q Tanks.jar
IF %1==clean EXIT
MKDIR build
javac -source 1.6 -target 1.6 -deprecation -sourcepath src -d build -implicit:class -g:none src\main\java\thebombzen\tanks\Tanks.java
jar -cmf jar_manifest.mf Tanks.jar -C build thebombzen

