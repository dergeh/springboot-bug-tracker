package bugTracker.service;

import bugTracker.dao.TicketRepo;
import bugTracker.model.Ticket;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
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
    public boolean deleteById(String id) {
        Optional<Ticket> ticketOptional = ticketRepo.findById(id);
        ticketRepo.deleteById(id);
        return ticketOptional.isPresent();
    }

    @Override
    public Ticket update(String id, Ticket ticket) {
        Optional<Ticket> ticketOptional = ticketRepo.findById(id);
        if (ticketOptional.isPresent()) return ticketRepo.save(ticket);
        return null;
    }

    @Override
    public Ticket findById(String id) {
        Optional<Ticket> ticketOptional = ticketRepo.findById(id);
        if (ticketOptional.isPresent()) return ticketOptional.get();
        return null;
    }

    @Override
    public List<Ticket> getAll() {
        return ticketRepo.findAll();
    }
}
