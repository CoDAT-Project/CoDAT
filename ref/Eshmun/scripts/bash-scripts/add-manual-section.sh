#!/bin/bash
marker="<!-- Add Section -->"
section_code="<a href=\"$2\">$1<\/a> <br\/><br\/>\n        ${marker}"

for f in $(find ../../src/usermanual | grep "\.html"); do
	sed -i "s/${marker}/${section_code}/g" $f
done

touch ../../src/usermanual/$2
