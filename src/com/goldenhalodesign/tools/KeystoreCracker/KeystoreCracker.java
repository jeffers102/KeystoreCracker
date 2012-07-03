package com.goldenhalodesign.tools.KeystoreCracker;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.security.KeyStore;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.StringTokenizer;
import java.util.Vector;

public class KeystoreCracker {
    private Segment[] segments;
    private Vector<String> passwords = new Vector<String>();
    private String keystoreLocation;
    private boolean passwordFound = false;
    private int attempts = 0;
    private int updateInterval = 500;

    public KeystoreCracker(Segment[] segments, String keystoreLocation) {
        setSegments(segments);
        this.keystoreLocation = keystoreLocation;
    }

    public KeystoreCracker(String segmentLocation, String keystoreLocation) {
        setSegments(processSegmentFile(segmentLocation));
        this.keystoreLocation = keystoreLocation;
    }

    private Segment[] processSegmentFile(String segmentLocation) {
        ArrayList<Segment> segments = new ArrayList<Segment>();

        try {
            FileInputStream fis = new FileInputStream(segmentLocation);
            DataInputStream in = new DataInputStream(fis);
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            String line;
            Segment currentSegment;
            while ((line = br.readLine()) != null) {
                currentSegment = processSegmentLine(line);
                if (currentSegment == null) {
                    // TODO replace with multiple segments arrays
                    continue;
                }

                segments.add(currentSegment);
            }
            in.close();
        } catch (FileNotFoundException e) {
            // if the file isn't found, terminate
            System.err.println(e);
            //System.exit(0);
        } catch (IOException e) {
            // problem reading file, terminate
            System.err.println(e);
            //System.exit(0);
        } catch (Exception e) {
            // this code should never execute, if so, terminate
            System.err.println(e);
            //System.exit(0);
        }

        return segments.toArray(new Segment[segments.size()]);
    }

    private Segment processSegmentLine(String rawLine) {
        String cleanLine = rawLine.trim();

        if (cleanLine.length() == 0) {
            return null;
        }

        StringTokenizer tokens = new StringTokenizer(cleanLine, ",");
        String token;
        String[] segments = new String[tokens.countTokens()];
        int i = 0;
        while (tokens.hasMoreTokens()) {
            token = tokens.nextToken();
            segments[i++] = token.trim();
        }

        boolean optional = false;
        if (rawLine.indexOf(" ") == 0) {
            optional = true;
        }

        return new Segment(segments, optional);
    }

    public boolean testPassword(String password) {
        this.attempts++;
        try {
            KeyStore ks = KeyStore.getInstance(KeyStore.getDefaultType());
            FileInputStream fis = new FileInputStream(keystoreLocation);
            ks.load(fis, password.toCharArray());
            fis.close();
            this.passwordFound = true;
            System.out.println("\nPassword is (" + password + ").  Found after " + attempts + " attempts");
            return true;
        } catch (FileNotFoundException e) {
            // if the file isn't found, terminate
            System.err.println(e);
            //System.exit(0);
        } catch (IOException e) {
            // this exception means the password is wrong
            if (attempts % updateInterval == 0) {
                System.out.println("Password not found after " + attempts + " attempts");
            }
        } catch (Exception e) {
            // this code should never execute, if so, terminate
            System.err.println(e);
            //System.exit(0);
        }

        return false;
    }

    public void crackPassword() {
        Enumeration<String> elements = this.passwords.elements();
        while (elements.hasMoreElements() && !this.passwordFound) {
            testPassword(elements.nextElement());
        }

        if (!this.passwordFound) {
            System.out.println("\nSorry, the password could not be found.  Please try other segments.  Good luck!");
        }
    }

    public void generatePasswords() {
        generatePasswordsHelper("", 0);
    }

    private void generatePasswordsHelper(String password, int index) {
        Segment segment = segments[index];
        String currentPassword;
        for (int i = 0; i < segment.values.length; i++) {
            currentPassword = password + segment.values[i];
            if (index + 1 < segments.length) {
                generatePasswordsHelper(currentPassword, index + 1);
            } else {
                this.passwords.add(currentPassword);
            }
        }

        if (segment.optional) {
            if (index + 1 < segments.length) {
                generatePasswordsHelper(password, index + 1);
            } else {
                this.passwords.add(password);
            }
        }
    }

    public void setSegments(Segment[] segments) {
        // TODO remove
        for (int i = 0; i < segments.length; i++) {
            //System.out.println(segments[i] + "\n");
        }
        this.segments = segments;
        generatePasswords();
    }

    public void setUpdateInterval(int interval) {
        this.updateInterval = interval;
    }

    public static void printUsage() {
        System.out.println("KeyCracker requires you to specify the file locations\n"
                + "for the segments and keystore files.  Optionally, you can pass\n"
                + "an interval at which to get an update on how many attempts have\n"
                + "been made (default is 500).  Example usage:\n");
        System.out.println("java com.goldenhalodesign.tools.KeystoreCracker.KeystoreCracker -k test_keystore.ppk -s sample_segments.txt");
        System.out.println("java com.goldenhalodesign.tools.KeystoreCracker.KeystoreCracker -k test_keystore.ppk -s sample_segments.txt -u 1000");
        System.out.println("java -classpath bin/KeystoreCracker.jar com.goldenhalodesign.tools.KeystoreCracker.KeystoreCracker -k test_keystore.ppk -s sample_segments.txt -u 1000");
        System.out.println("java com.goldenhalodesign.tools.KeystoreCracker.KeystoreCracker -keystore-location test_keystore.ppk -segment-location sample_segments.txt");
        System.out.println("java com.goldenhalodesign.tools.KeystoreCracker.KeystoreCracker -keystore-location test_keystore.ppk -segment-location sample_segments.txt -update-interval 1000");
        System.out.println("java -classpath bin/KeystoreCracker.jar com.goldenhalodesign.tools.KeystoreCracker.KeystoreCracker -keystore-location test_keystore.ppk -segment-location sample_segments.txt -update-interval 1000");

    }

    public static void main(String[] args) {
        String keystoreFile = null;
        String segmentFile = null;
        int updateInterval = -1;

        int argsLength = args.length;
        String currentArg;
        String nextArg;
        for (int i = 0; i < argsLength; i++) {
            currentArg = args[i];
            if (i + 1 >= argsLength) {
                break;
            }
            i++;
            nextArg = args[i];

            if (currentArg.equals("-h") || currentArg.equals("-help")) {
                KeystoreCracker.printUsage();
                return;
            } else if (currentArg.equals("-k") || currentArg.equals("-keystore-location")) {
                keystoreFile = nextArg;
            } else if (currentArg.equals("-s") || currentArg.equals("-segment-location")) {
                segmentFile = nextArg;
            } else if (currentArg.equals("-u") || currentArg.equals("-update-interval")) {
                try {
                    updateInterval = Integer.parseInt(nextArg);
                } catch (NumberFormatException e) {
                    System.err.println("Only integer values can be specified for the update interval.  Using default of 500.");
                }
            }
        }

        if (keystoreFile == null) {
            System.err.println("Keystore file location must be passed.  Use the \"-k\" or \"-keystore-location\" option.\n");
            KeystoreCracker.printUsage();
            return;
        }

        if (segmentFile == null) {
            System.err.println("Segment file location must be passed.  Use the \"-s\" or \"-segment-location\" option.\n");
            KeystoreCracker.printUsage();
            return;
        }

        KeystoreCracker cracker = new KeystoreCracker(segmentFile, keystoreFile);
        // override update interval if passed in
        if (updateInterval > 0) {
            cracker.setUpdateInterval(updateInterval);
        }
        cracker.crackPassword();
    }
}