#!/bin/bash

PACKAGE_NAME="boomerang"
PACKAGE_VERSION="0.1"
SOURCE_DIR=$PWD/package
TEMP_DIR="./tmp"

mkdir -p $TEMP_DIR
mkdir -p $TEMP_DIR/DEBIAN
mkdir -p $TEMP_DIR/usr/local/var/lib/$PACKAGE_NAME
mkdir -p $TEMP_DIR/usr/local/etc/$PACKAGE_NAME
#mkdir -p $TEMP_DIR/usr/local/etc/init.d
mkdir -p $TEMP_DIR/usr/local/var/log/$PACKAGE_NAME


echo "Package: $PACKAGE_NAME" > $TEMP_DIR/DEBIAN/control
echo "Version: $PACKAGE_VERSION" >> $TEMP_DIR/DEBIAN/control
cat $SOURCE_DIR/DEBIAN/control >> $TEMP_DIR/DEBIAN/control

echo  "Removing old jar"
rm -rf boomerang/build/libs/*.jar

echo  "Building new shadowJar"
./gradlew shadowJar

echo "Copying jar "
cp build/libs/boomerang-all.jar $TEMP_DIR/usr/local/var/lib/$PACKAGE_NAME/boomerang-shaded.jar

echo "Building debian"
dpkg-deb --build $TEMP_DIR

echo "Removing old .deb"
rm -f $SOURCE_DIR/*.deb

echo "Moving deb to package folder"
mv $TEMP_DIR.deb $SOURCE_DIR/$PACKAGE_NAME-$PACKAGE_VERSION.deb

echo "Removing temp folder"
rm -r $TEMP_DIR

