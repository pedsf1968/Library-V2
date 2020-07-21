package com.library.libraryapi.service;

import java.util.List;

public interface GenericMediaService<T,S,N> {
   boolean existsById(N id);
   T findById(N ean);

   List<T> findAll();
   List<T> findAllAllowed();
   List <T> findAllFiltered(T filter);

   N getFirstId(T filter);
   T save(T dto);
   T update(T dto);
   void deleteById(N id);
   Integer count();
   T entityToDTO(S entity);
   S dtoToEntity(T dto);
}
