package bugTracker.service;

import bugTracker.model.Ticket;

public interface TicketService {

   Ticket create(Ticket ticket);
   boolean delete(String id);
   Ticket update(String id, Ticket ticket);
   Ticket get(String id);
}
