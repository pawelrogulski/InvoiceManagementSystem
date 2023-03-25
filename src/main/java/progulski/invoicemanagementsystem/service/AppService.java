package progulski.invoicemanagementsystem.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import progulski.invoicemanagementsystem.domain.Invoice;
import progulski.invoicemanagementsystem.domain.Item;
import progulski.invoicemanagementsystem.repository.ItemRepository;
import progulski.invoicemanagementsystem.domain.Role;
import progulski.invoicemanagementsystem.domain.User;
import progulski.invoicemanagementsystem.repository.InvoiceRepository;
import progulski.invoicemanagementsystem.repository.RoleRepository;
import progulski.invoicemanagementsystem.repository.UserRepository;

import javax.persistence.EntityExistsException;
import java.rmi.NoSuchObjectException;
import java.time.LocalDate;
import java.util.*;

@RequiredArgsConstructor
@Service
public class AppService {
    private final InvoiceRepository invoiceRepository;
    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;

    public Role addRole(String role){
        String[] possibleRoles = {"ROLE_CUSTOMER","ROLE_EMPLOYEE"}; //lista wszystkich zaimplementowanych w aplikacji ról
        if(!Arrays.asList(possibleRoles).contains(role)){
            throw new IllegalStateException("Not implemented role");
        }
        Role foundRole = roleRepository.findByName(role);
        if(foundRole==null){ //jeśli zaimplementowanej roli nie ma jeszcze w bazie danych
            Role newRole = new Role();
            newRole.setName(role);
            roleRepository.save(newRole);
            foundRole = roleRepository.findByName(role);
        }
        return foundRole;
    }

    public User addCustomer(User user){
        return addUser(user,"ROLE_CUSTOMER");
    }
    public User addEmployee(User user){
        return addUser(user,"ROLE_EMPLOYEE");
    }
    public User addUser(User user,String role){
        if(!validateUsernameAndPassword(user.getUsername(), user.getPassword())){
            throw new IllegalStateException("Forbidden sign");
        }
        if(checkIfUsernameIsTaken(user.getUsername())){
            throw new EntityExistsException("Login in use");
        }

        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        Collection<Role> defaultRole = new ArrayList<>();
        defaultRole.add(addRole(role));
        user.setRoles(defaultRole);
        return userRepository.save(user);
    }
    public Boolean validateUsernameAndPassword(String username, String password){
        if(username.contains(" ")){return false;}
        if(password.contains(" ")){return false;}
        if(username.equals("")){return false;}
        if(password.equals("")){return false;}
        return true;
    }
    public Boolean checkIfUsernameIsTaken(String username){
        return !userRepository.findByUsername(username).equals(Optional.empty());
    }

    public void addRoleToUser(String username, String role){
        User user = userRepository.findByUsername(username).get();
        Collection<Role> roles = user.getRoles();
        roles.add(addRole(role));
        user.setRoles(roles);
        userRepository.save(user);
    }

    public Invoice addInvoice(Invoice invoice) {
        invoice.getItems().stream().forEach(item -> item.setTotalPrice());
        invoice.setTotalPrice();
        final Invoice savedInvoice = invoiceRepository.save(invoice);
        invoice.getItems().stream().forEach(item -> item.setInvoice(savedInvoice));
        invoice.getItems().stream().forEach(item -> addItem(item));
        return savedInvoice;
    }
    public void addItem(Item item){
        itemRepository.save(item);
    }

    public Invoice getInvoice(int id) throws NoSuchObjectException {
        Optional<Invoice> invoice = invoiceRepository.findById(id);
        if(invoice.isPresent()){
            return invoice.get();
        }
        else{
            throw new NoSuchObjectException("Invoice doesn't exist");
        }
    }

    public Set<Invoice> getInvoicesFromCurrentMonth() {
        LocalDate startDate = LocalDate.now().withDayOfMonth(1);//firstDayOfMonth to dzisiejsza data ze zmienionym dniem na 1
        return invoiceRepository.getInvoicesAfterDate(startDate);
    }

    public List<Object> groupByNipOderByOccurrences(){
        return invoiceRepository.groupByNipOderByOccurrences();
    }

    public List<Object> groupByNipOderByTotalPrice() {
        return invoiceRepository.groupByNipOderByTotalPrice();
    }

    public List<Invoice> sortByDate() {
        return invoiceRepository.sortByDate();
    }
    public List<Invoice> sortByNIP() {
        return invoiceRepository.sortByNIP();
    }
    public List<Invoice> sortByTotalPrice() {
        return invoiceRepository.sortByTotalPrice();
    }
    public List<Object> countByCity() {
        return invoiceRepository.countByCity();
    }
    public List<Object> sumByCity() {
        return invoiceRepository.sumByCity();
    }

    public List<Object> groupByItems() {
        return invoiceRepository.groupByItems();
    }
}
