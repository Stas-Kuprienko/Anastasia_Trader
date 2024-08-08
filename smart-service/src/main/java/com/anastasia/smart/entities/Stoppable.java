/*
 * Stanislav Kuprienko *** Omsk, Russia
 */

package com.anastasia.smart.entities;

public interface Stoppable {
    void addConsumer(Object o);

    void removeConsumer(Object o);

    boolean isUseless();

    boolean stop();
}
