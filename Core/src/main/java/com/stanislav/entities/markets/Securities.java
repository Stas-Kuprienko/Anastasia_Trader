package com.stanislav.entities.markets;

import java.time.LocalDateTime;

public interface Securities {

    record PriceAtTheTime(double price, LocalDateTime time) {}
}
