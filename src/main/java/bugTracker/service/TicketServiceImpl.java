package bugTracker.service;

import bugTracker.dao.TicketRepo;
import bugTracker.model.Ticket;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class TicketServiceImpl implements TicketService {

    @Autowired
    TicketRepo ticketRepo;



    @Override
    public Ticket create(Ticket ticket) {
        return ticketRepo.save(ticket);
    }

    @Override
    public boolean delete(String id) {
        return false;
    }

    @Override
    public Ticket update(String id, Ticket ticket) {
        Optional<Ticket> ticketOptional = ticketRepo.findById(id);
        if(ticketOptional.isPresent())return ticketRepo.save(ticket);
        return null;
    }

    @Override
    public Ticket get(String id) {
        Optional<Ticket> ticketOptional = ticketRepo.findById(id);
        if(ticketOptional.isPresent())return ticketOptional.get();
        return null;
    }
}
