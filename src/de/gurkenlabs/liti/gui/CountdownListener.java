package de.gurkenlabs.liti.gui;

import java.util.EventListener;

public interface CountdownListener extends EventListener {

  default void started() {
  }

  default void stopped() {
  }

  default void secondPassed() {
  }
}
