import bugTracker.dao.TicketRepo;
import bugTracker.model.Ticket;
import bugTracker.service.TicketServiceImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class ServiceTest {

    private final Ticket testTicket = new Ticket("test", "test ticket", true);
    @InjectMocks
    TicketServiceImpl service;

    @Mock
    TicketRepo dao;

    @Test
    public void createTest() {
        when(dao.save(any())).thenReturn(testTicket);

        Ticket created = service.create(testTicket);
        assertEquals("test", created.getId());
        assertEquals("test ticket", created.getDescription());
        assertTrue(created.isOpen());

        verify(dao, times(1)).save(any());


    }

    @Test
    public void getByIdTest() {
        when(dao.findById(eq("test"))).thenReturn(Optional.of(testTicket));


        //ticket is present
        Ticket found = service.findById(testTicket.getId());
        assertEquals("test", found.getId());
        assertEquals("test ticket", found.getDescription());
        assertTrue(found.isOpen());

        verify(dao, times(1)).findById(any());


    }

    @Test
    public void getByIdFailTest() {
        when(dao.findById(anyString())).thenReturn(Optional.empty());

        // ticket is not present
        Ticket notFound = service.findById("no");
        assertNull(notFound);

        verify(dao, times(1)).findById(any());
    }

    @Test
    public void updateTest() {
        Ticket updatedTicket = new Ticket("test", "updated", false);
        when(dao.findById(eq("test"))).thenReturn(Optional.of(testTicket));
        when(dao.save(any())).thenReturn(updatedTicket);

        Ticket updated = service.update(updatedTicket.getId(), updatedTicket);
        assertEquals("test", updated.getId());
        assertEquals("updated", updated.getDescription());
        assertFalse(updated.isOpen());

        verify(dao, times(1)).save(any());


    }
}
