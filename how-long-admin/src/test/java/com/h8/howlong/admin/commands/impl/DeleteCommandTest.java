package com.h8.howlong.admin.commands.impl;

import com.h8.howlong.admin.commands.CommandResultStatus;
import com.h8.howlong.admin.services.TimesheetManagementFailedException;
import com.h8.howlong.admin.services.TimesheetManagementService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.time.LocalDateTime;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.*;

class DeleteCommandTest {

    private TimesheetManagementService timesheetManagementService;

    private DeleteCommand deleteCommand;

    private int month;
    private int day;

    @BeforeEach
    void setUp() {
        timesheetManagementService = mock(TimesheetManagementService.class);
        var current = LocalDateTime.now();
        month = current.getMonthValue();
        day = current.getDayOfMonth();
        deleteCommand = new DeleteCommand(timesheetManagementService, month, day);
    }

    @Test
    void shouldCallDeleteOnTimesheetManagementServiceAndReturnCommandResultOk()
            throws TimesheetManagementFailedException {
        //when
        var commandResult = deleteCommand.execute();

        //then
        var monthCaptor = ArgumentCaptor.forClass(Integer.class);
        var dayCaptor = ArgumentCaptor.forClass(Integer.class);
        verify(timesheetManagementService, times(1))
                .delete(monthCaptor.capture(), dayCaptor.capture());

        assertThat(monthCaptor.getValue()).isEqualTo(month);
        assertThat(dayCaptor.getValue()).isEqualTo(day);
        assertThat(commandResult)
                .hasFieldOrPropertyWithValue("message",
                        String.format("The day '%d.%d' has been deleted", day, month))
                .hasFieldOrPropertyWithValue("status", CommandResultStatus.SUCCESS);
    }

    @Test
    void shouldReturnCommandResultErrorWhenTimeSheetManagementExceptionIsThrown()
            throws TimesheetManagementFailedException {
        //when
        doThrow(new TimesheetManagementFailedException("test"))
                .when(timesheetManagementService).delete(month, day);

        var commandResult = deleteCommand.execute();

        //then
        assertThat(commandResult)
                .hasFieldOrPropertyWithValue("message",
                        String.format("The day '%d.%d' could not be deleted because of an exception: test", day, month))
                .hasFieldOrPropertyWithValue("status", CommandResultStatus.ERROR);
    }
}