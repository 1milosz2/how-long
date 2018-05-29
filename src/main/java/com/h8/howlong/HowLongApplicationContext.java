package com.h8.howlong;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.h8.howlong.printers.PrintingServiceFactory;
import com.h8.howlong.services.TimesheetService;
import lombok.Getter;

@Getter
@Singleton
class HowLongApplicationContext {

    private final TimesheetService timesheetService;

    private final PrintingServiceFactory printingServiceFactory;

    @Inject
    public HowLongApplicationContext(
            TimesheetService timesheetService,
            PrintingServiceFactory printingServiceFactory) {
        this.timesheetService = timesheetService;
        this.printingServiceFactory = printingServiceFactory;
    }

}