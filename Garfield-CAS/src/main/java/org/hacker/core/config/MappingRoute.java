package org.hacker.core.config;

import org.hacker.module.cas.controller.CasController;

import com.jfinal.config.Routes;

/**
 * Generated by JFinal, do not modify this file.
 * <pre>
 * Example:
 * public void configRoute(Routes me) {
 *   MappingRoute.mapping(me);
 * }
 * </pre>
 */
public class MappingRoute {

  public static void mapping(Routes me) {
    me.add("/cas", CasController.class);
  }
  
}
