#!/bin/bash
javadoc -d dist/docs -J-Xmx100M -classpath ../Vault.jar:../craftbukkit-1.1-R3.jar -sourcepath src -subpackages com
