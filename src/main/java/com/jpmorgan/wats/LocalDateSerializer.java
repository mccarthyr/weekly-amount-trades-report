package com.jpmorgan.wats;


import java.time.*;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;

import com.fasterxml.jackson.databind.ser.std.*;


import java.io.*;
import java.time.format.*;



public class LocalDateSerializer extends StdSerializer<LocalDate> {

    private static final long serialVersionUID = 1L;

    public LocalDateSerializer(){
        super(LocalDate.class);
    }

    @Override
    public void serialize(LocalDate value, JsonGenerator gen, SerializerProvider sp) throws IOException, JsonProcessingException {
        gen.writeString(value.format(DateTimeFormatter.ISO_LOCAL_DATE));
    }
}