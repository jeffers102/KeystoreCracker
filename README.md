KeystoreCracker
===============

Helps retrieve forgotten keystore passwords using your commonly used segments.

For instance, what if you remember that your password was "test-password" or "te5T-P@SSWORD"
or "Testpassw0rd" or something close to that?  It would be horribly ineffective to try more
than a handful of variants, both in typing in the possible passwords and remembering which
you have tried already.  Enter KeystoreCracker!

KeystoreCracker is NOT a brute force tool that will help you crack someone else's keystore, but
rather a way to help YOU recover a forgotten password when you are a bit fuzzy on a few
characters.

Usage:
Let's say that you sometimes swap the number zero and the letter O and sometimes use
caps and sometimes a hyphen to separate words.  In this case, let's assume that your password
is some form of "foobar".  You remember that the "bar" section is typed just like that, but
can't remember the other cases.  To find the solution, simply create a file with the segments in
order.  For optional segments, preface the line with a blank space.  For instance, in the scenario
above, your file would like this:

f, F
o, O, 0
o, O, 0
 -
bar

Notice that the line with the hypen, has a blank space, so that segment is optional.  This would try
all combinations, such as:

foo-bar, F0Obar, F00-bar, etc

Now that the file is setup, you just need to execute the program using either the class or the JAR
(this assumes that your keystore and segment file are in the current directory):

java com.goldenhalodesign.tools.KeystoreCracker.KeystoreCracker -k test_keystore.ppk -s sample_segments.txt
java -classpath bin/KeystoreCracker.jar com.goldenhalodesign.tools.KeystoreCracker.KeystoreCracker -k test_keystore.ppk -s sample_segments.txt