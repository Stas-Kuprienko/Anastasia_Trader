/*
 * Stanislav Kuprienko *** Omsk, Russia
 */

package com.stanislav.smart.entities;

public interface Stoppable {
    void addConsumer(Object o);

    void removeConsumer(Object o);

    boolean isUseless();

    boolean stop();
}
