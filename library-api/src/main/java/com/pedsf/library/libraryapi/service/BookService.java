package com.pedsf.library.libraryapi.service;

import com.pedsf.library.dto.business.BookDTO;
import com.pedsf.library.dto.business.PersonDTO;
import com.pedsf.library.exception.*;
import com.pedsf.library.libraryapi.model.Book;
import com.pedsf.library.libraryapi.repository.BookRepository;
import com.pedsf.library.libraryapi.repository.BookSpecification;
import org.modelmapper.ModelMapper;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

@Service("BookService")
public class BookService implements GenericMediaService<BookDTO,Book,String> {
   private static final String CANNOT_FIND_WITH_ID = "Cannot find Book with the EAN : ";
   private static final String CANNOT_SAVE ="Failed to save Book";

   private final BookRepository bookRepository;
   private final PersonService personService;
   private final ModelMapper modelMapper = new ModelMapper();

   public BookService(BookRepository bookRepository, PersonService personService) {
      this.bookRepository = bookRepository;
      this.personService = personService;
   }


   @Override
   public boolean existsById(String ean) {
      return bookRepository.findByEan(ean).isPresent();
   }

   @Override
   public BookDTO findById(String ean) {

      if (existsById(ean)) {
         Book book = bookRepository.findByEan(ean).orElse(null);
         return entityToDTO(book);
      } else {
         throw new ResourceNotFoundException(CANNOT_FIND_WITH_ID + ean);
      }
   }

   @Override
   public List<BookDTO> findAll() {
      List<Book> books = bookRepository.findAll();
      List<BookDTO> bookDTOS = new ArrayList<>();

      for (Book book: books){
         bookDTOS.add(entityToDTO(book));
      }

      if (!bookDTOS.isEmpty()) {
         return bookDTOS;
      } else {
         throw new ResourceNotFoundException();
      }
   }

   @Override
   public List<BookDTO> findAllAllowed() {
      List<Book> books = bookRepository.findAllAllowed();
      List<BookDTO> bookDTOS = new ArrayList<>();

      for (Book book: books){
         bookDTOS.add(entityToDTO(book));
      }

      if (!bookDTOS.isEmpty()) {
         return bookDTOS;
      } else {
         throw new ResourceNotFoundException();
      }
   }

   @Override
   public List<BookDTO> findAllFiltered(BookDTO filter) {
      Book book = dtoToEntity(filter);

      Specification<Book> spec = new BookSpecification(book);
      List<Book> books = bookRepository.findAll(spec);
      List<BookDTO> bookDTOS = new ArrayList<>();

      for ( Book b: books){
         bookDTOS.add(entityToDTO(b));
      }

      if (!bookDTOS.isEmpty()) {
         return bookDTOS;
      } else {
         throw new ResourceNotFoundException();
      }
   }

   @Override
   public String getFirstId(BookDTO filter){

      return findAllFiltered(filter).get(0).getEan();
   }


   @Override
   public BookDTO save(BookDTO bookDTO) {
      if (  !StringUtils.isEmpty(bookDTO.getTitle()) &&
            !StringUtils.isEmpty(bookDTO.getAuthor()) &&
            !StringUtils.isEmpty(bookDTO.getEditor()) &&
            !StringUtils.isEmpty(bookDTO.getType()) &&
            !StringUtils.isEmpty(bookDTO.getFormat())) {

         try {
            // try to find existing Book
            String ean = getFirstId(bookDTO);
            Book book = bookRepository.findByEan(ean).orElse(null);
            return entityToDTO(book);
         } catch (ResourceNotFoundException ex) {
            return entityToDTO(bookRepository.save(dtoToEntity(bookDTO)));
         }

      } else {
         throw new BadRequestException(CANNOT_SAVE);
      }
   }

   @Override
   public BookDTO update(BookDTO bookDTO) {
      if (  !StringUtils.isEmpty(bookDTO.getEan()) &&
            !StringUtils.isEmpty(bookDTO.getTitle()) &&
            !StringUtils.isEmpty(bookDTO.getAuthor()) &&
            !StringUtils.isEmpty(bookDTO.getEditor()) &&
            !StringUtils.isEmpty(bookDTO.getType()) &&
            !StringUtils.isEmpty(bookDTO.getFormat())) {
         if (!existsById(bookDTO.getEan())) {
            throw new ResourceNotFoundException(CANNOT_FIND_WITH_ID + bookDTO.getEan());
         }
         Book book = bookRepository.save(dtoToEntity(bookDTO));

         return entityToDTO(book);
      } else {
         throw new ConflictException(CANNOT_SAVE);
      }
   }


   @Override
   public void deleteById(String ean) {
      if (!existsById(ean)) {
         throw new ResourceNotFoundException(CANNOT_FIND_WITH_ID + ean);
      } else {
         bookRepository.deleteByEan(ean);
      }
   }

   @Override
   public Integer count() {
      return Math.toIntExact(bookRepository.count());
   }

   @Override
   public BookDTO entityToDTO(Book book) {
      BookDTO bookDTO = modelMapper.map(book, BookDTO.class);
      PersonDTO author = personService.findById(book.getAuthorId());
      PersonDTO editor = personService.findById(book.getEditorId());

      bookDTO.setAuthor(author);
      bookDTO.setEditor(editor);
      bookDTO.setPublicationDate(book.getPublicationDate());

      return bookDTO;
   }

   @Override
   public Book dtoToEntity(BookDTO bookDTO) {
      Book book = modelMapper.map(bookDTO, Book.class);

      if(bookDTO.getAuthor()!=null) {
         book.setAuthorId(bookDTO.getAuthor().getId());
      }
      if(bookDTO.getEditor()!=null) {
         book.setEditorId(bookDTO.getEditor().getId());
      }

      book.setPublicationDate(bookDTO.getPublicationDate());
      return book;
   }

      public List<PersonDTO> findAllAuthors() {
      List<PersonDTO> personDTOS = new ArrayList<>();

      for (int auhorId:bookRepository.findAllAuthors()){
         personDTOS.add(personService.findById(auhorId));
      }

      return personDTOS;
   }

   public List<PersonDTO> findAllEditors() {
      List<PersonDTO> personDTOS = new ArrayList<>();

      for (int editorId:bookRepository.findAllEditors()){
         personDTOS.add(personService.findById(editorId));
      }

      return personDTOS;
   }

   public List<String> findAllTitles() {
      return bookRepository.findAllTitles();
   }

   void increaseStock(String ean) {
      bookRepository.increaseStock(ean);
   }

   void decreaseStock(String ean) {
      bookRepository.decreaseStock(ean);
   }

}
