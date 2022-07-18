package study.datajpa.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import study.datajpa.entity.Item;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ItemRepositoryTest {

    @Autowired ItemRepository itemRepository;

    @Test
    public void save() throws Exception {
        //given
        Item item = new Item(1L);

        //when
        itemRepository.save(item);
        Item findItem = itemRepository.findById(item.getId()).get();

        //then
        assertEquals(1, findItem.getId());
    }
}