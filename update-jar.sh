#!/bin/bash

# Variables
jarfile="FileIdentify-0.0.1-SNAPSHOT.jar"  # Based on your Maven artifactId and version
destination="$HOME/Desktop/"  # Copying the jar to the user's Desktop

# Navigate to the directory of the script
cd "$(dirname "$0")"

echo "Building the project..."
mvn package

# Check if Maven build was successful
if [ $? -ne 0 ]; then
    echo "Maven build failed! Exiting..."
    exit 1
fi

echo
echo "Copying $jarfile to $destination"

# Copy the new jar file
echo "Copying new jar..."
cp -f "target/$jarfile" "$destination"

echo "Done."

# Pause the script for user to press any key to continue
read -p "Press any key to continue..." -n1 -s
