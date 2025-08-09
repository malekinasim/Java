package com.example.employee.task.tracker.config.hibernate;

import com.example.employee.task.tracker.model.Organ;

public class OrganContext {
    private static final ThreadLocal<Organ> currentOrgan = new ThreadLocal<>();

    public static void setOrgan(Organ organ) {
        currentOrgan.set(organ);
    }

    public static Organ getOrgan() {
        return currentOrgan.get();
    }

    public static void clear() {
        currentOrgan.remove();
    }
}

