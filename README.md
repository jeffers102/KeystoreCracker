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
can't remember the other cases.  To find the solution, simply create an array of Segments as
follows (this assumes you are inside JAVA):

// setup the segments in the correct order

Segment[] segments = {

    new Segment(new String[]{ "f", "F" }),
    
    new Segment(new String[]{ "o", "O", "0"}),
    
    new Segment(new String[]{ "o", "O", "0"}),
    
    new Segment(new String[]{ "-", true}), // true means optional, test with and without
    
    new Segment(new String[]{ "bar"}), // bar has no variants, hence the one entry
    
}


// create an instance of the KeystoreCracker

KeystoreCracker cracker = new KeystoreCracker(segments, "test_keystore.ppk");

// default is 100 - after every X attempts (where X is the update interval), let the user know

//                  how many password variants have been attempted

cracker.setUpdateInterval(10);

// get to work and find the password!!

cracker.crackPassword();
