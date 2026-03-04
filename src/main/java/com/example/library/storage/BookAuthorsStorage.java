package com.example.library.storage;

import java.util.List;

public interface BookAuthorsStorage {
    void addAuthorToBook( long bookId, long authorId);
    boolean removeAuthorFromBook( long bookId, long authorId);
    List<Long> authorIdsForBook(long bookId);
}
