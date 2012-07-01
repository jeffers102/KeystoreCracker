package com.goldenhalodesign.tools.KeystoreCracker;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.KeyStore;
import java.util.Enumeration;
import java.util.Vector;

public class KeystoreCracker {
    private Segment[] segments;
    private Vector<String> passwords;
    private String privateKeyLocation;
    private boolean passwordFound = false;
    private int attempts = 0;
    private int updateInterval = 100;

    public KeystoreCracker(Segment[] segments, String fileLocation) {
        this.passwords = new Vector<String>();
        setSegments(segments);
        this.privateKeyLocation = fileLocation;
    }

    public boolean testPassword(String password) {
        this.attempts++;
        try {
            KeyStore ks = KeyStore.getInstance(KeyStore.getDefaultType());

            // get user password and file input stream
            FileInputStream fis = new FileInputStream(privateKeyLocation);
            ks.load(fis, password.toCharArray());
            fis.close();
            this.passwordFound = true;
            System.out.println("Password is (" + password + ").  Found after " + attempts + " attempts");
            return true;
        } catch (FileNotFoundException e) {
            System.out.println(e);
        } catch (IOException e) {
            if (attempts % updateInterval == 0) {
                System.out.println("Password not found after " + attempts + " attempts");
            }
        } catch (Exception e) {
            System.out.println(e);
        }

        return false;
    }

    public void crackPassword() {
        Enumeration<String> elements = this.passwords.elements();
        while (elements.hasMoreElements() && !this.passwordFound) {
            testPassword(elements.nextElement());
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
        this.segments = segments;
        generatePasswords();
    }

    public void setUpdateInterval(int interval) {
        this.updateInterval = interval;
    }

    public static void main(String[] args) {
        Segment[] segments = {
                new Segment(new String[]{"t", "T"}),
                new Segment(new String[]{"e", "E"}),
                new Segment(new String[]{"s", "S", "5"}),
                new Segment(new String[]{"t", "T"}),
                new Segment(new String[]{"-"}, true),
                new Segment(new String[]{"p", "P"}),
                new Segment(new String[]{"a", "A", "@"}),
                new Segment(new String[]{"s", "S", "5"}),
                new Segment(new String[]{"s", "S", "5"}),
                new Segment(new String[]{"w", "W"}),
                new Segment(new String[]{"o", "O", "0"}),
                new Segment(new String[]{"r", "R"}),
                new Segment(new String[]{"d", "D"})
        };

        // Te5t-P@sSw0rD
        KeystoreCracker cracker = new KeystoreCracker(segments, "test_keystore.ppk");
        // default is 100
        cracker.setUpdateInterval(1000);
        cracker.crackPassword();
    }
}