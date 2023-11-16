/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Modelo;

import java.util.prefs.Preferences;

/**
 *
 * @author dmesc
 */
public class AlmacenamientoRenta {
        private static final String CLAVE_RENTA = "rentaReserva";

        public static void guardarRenta(double monto) {
            Preferences prefs = Preferences.userNodeForPackage(AlmacenamientoRenta.class);
            prefs.putDouble(CLAVE_RENTA, monto);
        }

        public static double obtenerRenta() {
            Preferences prefs = Preferences.userNodeForPackage(AlmacenamientoRenta.class);
            return prefs.getDouble(CLAVE_RENTA, 0.0); // Valor por defecto si no se encuentra
        }
    }
