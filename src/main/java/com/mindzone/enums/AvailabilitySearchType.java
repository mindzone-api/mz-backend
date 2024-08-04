package com.mindzone.enums;

/**
 * This enum is used to specify the availability attribute in the SearchFilter,
 * allowing the patient to choose between searching for professional available on
 * Tuesday 08AM - 10AM >AND< 02PM - 04PM (if the SearchType is ALL)
 * or
 * Tuesday 08AM - 10AM >OR< 02PM - 04PM, (if the SearchType is ANY)
 * for example
 */
public enum AvailabilitySearchType {
    ALL,
    ANY
}
