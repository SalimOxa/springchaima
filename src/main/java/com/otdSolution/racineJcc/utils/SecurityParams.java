package com.otdSolution.racineJcc.utils;

public interface SecurityParams {
    public static final String JWT_HEADER_NAME="Authorization";
    public static final String SECRET="ahmed@gmail.com";
    // expiration token dans 10 jours
    public static final long EXPIRATION=864_000_000;
    public static final String HEADER_PREFIX="Bearer ";
    public static final String Mail="otd.solution2021@gmail.com";

}
