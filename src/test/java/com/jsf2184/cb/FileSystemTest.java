package com.jsf2184.cb;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;

@Slf4j
public class FileSystemTest {

    public static class Node {
        boolean isDirectory;
        String name;
        HashMap<String, Node> childNodes;
        String data;

        public Node(String name) {
            this.name = name;
            childNodes = new HashMap<>();
            data = null;
            isDirectory = true;
        }

        public Node(String name, String data) {
            this.name = name;
            this.data = data;
        }

        Node findChild(String name) {
            if (childNodes != null) {
                return childNodes.get(name);
            }
            return null;
        }

        void addChildNode(Node child) throws Exception {
            if (!isDirectory) {
                throw new Exception("path is not a directory");
            }
            if (childNodes.get(child.name) != null) {
                throw new Exception("path already exists");
            }
            childNodes.put(child.name, child);
        }
    }


    @Data
    public static class Path {
        ArrayList<String> pathParts;
        String filename;

        public Path(ArrayList<String> pathParts, String filename) {
            this.pathParts = pathParts;
            this.filename = filename;
        }

        public static Path createPath(String path) throws Exception {
            if (path == null || path.length() == 0) {
                throw new Exception("bad path");
            }
            ArrayList<String> pathParts = new ArrayList<>();
            if (path.startsWith("/")) {
                pathParts.add("/");
            }
            final String[] partsArray = path.split("/");

            final int numParts = partsArray.length;

            for (int i = 0; i< numParts - 1; i++) {
                final String part = partsArray[i];
                if (part == null || part.length() == 0) {
                    if (i==0) {
                        continue;
                    }
                    throw new Exception("Unexpected empty path part");
                }
                pathParts.add(part);
            }
            String filename = null;
            if (numParts >= 1) {
                filename = partsArray[numParts - 1];
            }
            return new Path(pathParts, filename);
        }

        public boolean isRooted() {
            return  pathParts.size() >= 1 && pathParts.get(0).equals("/");
        }
    }



    public static class FileSystem {

        Node root = new Node("/");

        //*******************************************
        // Public Methods -- IE: The assignment
        //*******************************************

        public void mkdir(String pathName) throws Exception {
            Path path = Path.createPath(pathName);
            final Node parent = findParent(path);
            if (parent == null) {
                throw new Exception("bad path");
            }
            String dirName = path.filename;
            if (parent.findChild(dirName) != null) {
                throw new Exception("path is not a directory");
            }
            parent.addChildNode(new Node(dirName));
        }

        public void addFile(String pathName, String data) throws Exception {
            Path path = Path.createPath(pathName);
            final Node parent = findParent(path);
            if (parent == null) {
                throw new Exception("bad path");
            }
            String fileName =path.filename;
            parent.addChildNode(new Node(fileName, data));
        }

        public String getFileData(String pathName) throws Exception {
            Node node = findNode(pathName);
            if (node == null) {
                throw new Exception("could not find file");
            }
            if (node.isDirectory) {
                throw new Exception("file is a directory");
            }
            return node.data;
        }

        //*******************************************
        // Utility Methods
        //*******************************************

        Node findParent(Path path) {

            if (!path.isRooted() || path.filename == null) {
                return null;
            }

            Node current = root;
            final ArrayList<String> parts = path.getPathParts();
            for (int i=1; i<parts.size(); i++) {
                String part = parts.get(i);
                current = current.findChild(part);
                if (current == null) {
                    return null;
                }
            }
            return current;
        }

        Node findNode(String pathName) {
            try {
                Path path = Path.createPath(pathName);
                final Node parent = findParent(path);
                if (parent != null) {
                    return parent.findChild(path.filename);
                }
                return parent;
            } catch (Exception e) {
            }
            return null;
        }
    }

    @Test
    public void testCreatePath() throws Exception {
        Assert.assertEquals(new Path(new ArrayList<>(Collections.singletonList("/")), "a"), Path.createPath("/a"));
        Assert.assertEquals(new Path(new ArrayList<>(Arrays.asList ("/", "a")), "b"), Path.createPath("/a/b"));
        Assert.assertEquals(new Path(new ArrayList<>(Arrays.asList ("/", "a")), "b"), Path.createPath("/a/b/"));
        Assert.assertEquals(new Path(new ArrayList<>(Arrays.asList ("/", "a", "b")), "c"), Path.createPath("/a/b/c"));

        Assert.assertEquals(new Path(new ArrayList<>(), "a"), Path.createPath("a"));
        Assert.assertEquals(new Path(new ArrayList<>(Collections.singletonList("a")), "b"), Path.createPath("a/b"));
        Assert.assertEquals(new Path(new ArrayList<>(Arrays.asList("a", "b")), "c"), Path.createPath("a/b/c"));
    }

    @Test
    public void testCreateBadPath() {
        verifyBadPath(null);
        verifyBadPath("");
    }

    public void verifyBadPath(String path) {
        boolean caught = false;
        try {
            Path.createPath(path);
        } catch (Exception e) {
            caught = true;
        }
        Assert.assertTrue(caught);
    }

    @Test
    public void testScenario() throws Exception {

        // Use our callMkdir utility to call the real mkdir and verify for each call that
        // the FileSystem behaved as expected. That is, on success cases, that the
        // directory was really created and on bad mkdirs, that an exception was thrown.
        //
        FileSystem fileSystem = new FileSystem();
        callMkdir("/", fileSystem, true, null);
        callMkdir("x", fileSystem, true, null);
        callMkdir("/a/b", fileSystem, true, null);
        callMkdir("/a", fileSystem, false, "a");
        callMkdir("/a/b", fileSystem, false, "b");
        callMkdir("/a/c", fileSystem, false, "c");
        callMkdir("/a/b/c", fileSystem, false, "c");
        callMkdir("/x/b", fileSystem, true, null);

        // Lets try adding a file that should fail due to a bad path.
        addFile("/a/b/x/d.txt", fileSystem, true, null);
        // Lets try adding a file that should fail since the file already exists as a directory
        addFile("/a/b/c", fileSystem, true, null);

        // Finally lets add a good file and record the data placed into the file in writtenData variable
        final String writtenData = addFile("/a/b/c/d.txt", fileSystem, false, "d.txt");
        log.info("Wrote {} into file: /a/b/c/d.txt ", writtenData);

        // Now try to add it again which should fail since it already exists.
        addFile("/a/b/c/d.txt", fileSystem, true, null);

        // Finally try to retrieve it.
        final String retievedData = fileSystem.getFileData("/a/b/c/d.txt");
        Assert.assertEquals(writtenData, retievedData);

    }

    public String addFile(String path, FileSystem fileSystem, boolean expectException, String expectName) {
        boolean caught = false;
        String data = "data for: " + path;
        try {
            fileSystem.addFile(path, data);
        } catch (Exception e) {
            log.info("Caught mkdir exception: {}, expectException = {}", e.getMessage(), expectException);
            caught = true;
        }
        Assert.assertEquals(expectException, caught);
        if (!expectException) {
            Node node = fileSystem.findNode(path);
            Assert.assertEquals(expectName, node.name);
            Assert.assertEquals(data, node.data);
        }
        return data;
    }

    public void callMkdir(String path, FileSystem fileSystem, boolean expectException, String expectName) {
        boolean caught = false;
        try {
            fileSystem.mkdir(path);
        } catch (Exception e) {
            log.info("Caught mkdir exception: {}, expectException = {}", e.getMessage(), expectException);
            caught = true;
        }
        Assert.assertEquals(expectException, caught);
        if (!expectException) {
            Node node = fileSystem.findNode(path);
            Assert.assertEquals(expectName, node.name);
        }
    }
}
