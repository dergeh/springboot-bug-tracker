package bugTracker.service;

import bugTracker.model.Ticket;

import java.util.List;

public interface TicketService {

    Ticket create(Ticket ticket);

    boolean deleteById(String id);

    Ticket update(String id, Ticket ticket);

    Ticket findById(String id);

    List<Ticket> getAll();
}
