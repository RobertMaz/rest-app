package ru.demo.app.restapp.util;


import java.util.List;

public class Utility {

  private Utility() {
  }

  public static String getMessage(String template, Object... args) {
    for (int i = 0; i < args.length; i++) {
      template = template.replace("{" + (i + 1) + "}", toString(args[i]));
    }
    return template;
  }

  public static String toString(Object obj) {
    return obj == null ? "" : obj.toString();
  }

  public static boolean isEmpty(List<?> obj) {
    return obj == null || obj.isEmpty();
  }
}
