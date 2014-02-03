#! /usr/bin/env python
import pysvn

###########################################################
#
# This script is to create a snapshot of trunk. The goals
# of script are to take a trunk revision point and create a
# snapshot tag with the indicated snapshot sequence
# number.
#
# DEVELOPER NOTES:
# This script relies on the use of pysvn 1.7.2, which can be
# found at http://pysvn.tigris.org.
#
###########################################################

print "Deploying SNAPSHOT for proto-sloth"

## 1. Create a tag from trunk.
#   The tag will be designed as:
#
#       tag/proto-sloth-{version}-{snapshot_count}-SNAPSHOT
#

## 2. Change the pom.xml project version for parent and module poms.
#   The version for the individual modules will be determined as
#
#        <version>major.minor-snapshot_count-SNAPSHOT<version>
#

## 3. //todo: raul - finsih this
#
