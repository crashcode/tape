// Copyright 2014 Square, Inc.
package com.squareup.tape;

import java.io.File;
import java.io.IOException;
import java.util.List;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import static org.fest.assertions.Assertions.assertThat;

public class FileObjectQueueTest {
  @Rule public TemporaryFolder folder = new TemporaryFolder();
  private FileObjectQueue<String> queue;

  @Before public void setUp() throws IOException {
    File parent = folder.getRoot();
    File file = new File(parent, "queue-file");
    queue = new FileObjectQueue<String>(file, new SerializedConverter<String>());
    queue.add("one");
    queue.add("two");
    queue.add("three");
  }

  @Test public void peekMultiple() {
    List<String> peek = queue.peek(3);
    assertThat(peek).containsExactly("one", "two", "three");
  }

  @Test public void getsAllAsList() {
    List<String> peek = queue.asList();
    assertThat(peek).containsExactly("one", "two", "three");
  }

  @Test public void peekMaxCanExceedQueueDepth() {
    List<String> peek = queue.peek(6);
    assertThat(peek).hasSize(3);
  }

  @Test public void peekMaxCanBeSmallerThanQueueDepth() {
    List<String> peek = queue.peek(2);
    assertThat(peek).containsExactly("one", "two");
  }

  @Test public void checkForIntegritySanityCheckDoesNotThrow() throws IOException {
    queue.checkQueueIntegrity();
  }
}
