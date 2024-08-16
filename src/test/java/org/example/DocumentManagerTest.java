package org.example;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class DocumentManagerTest {
    private DocumentManager documentManager;

@BeforeEach
public void setUp() {
    documentManager = new DocumentManager();
}
    @Test
    public void saveNewDocumentTest() {
        DocumentManager.Document document = DocumentManager.Document.builder()
                .title("Test Title")
                .content("Test Content")
                .author(new DocumentManager.Author(UUID.randomUUID().toString(), "Author Name"))
                .created(Instant.now())
                .build();

        DocumentManager.Document savedDocument = documentManager.save(document);

        assertNotNull(savedDocument.getId());
        assertEquals(document.getTitle(), savedDocument.getTitle());
        assertEquals(document.getContent(), savedDocument.getContent());
        assertEquals(document.getAuthor(), savedDocument.getAuthor());
        assertEquals(document.getCreated(), savedDocument.getCreated());
    }
    @Test
    public void testUpdateDocument() {
        DocumentManager.Document document = DocumentManager.Document.builder()
                .id(UUID.randomUUID().toString())
                .title("Test Title")
                .content("Test Content")
                .author(new DocumentManager.Author(UUID.randomUUID().toString(), "Author Name"))
                .created(Instant.now())
                .build();

        documentManager.save(document);

        DocumentManager.Document updatedDocument = DocumentManager.Document.builder()
                .id(document.getId())
                .title("Updated Title")
                .content("Updated Content")
                .author(document.getAuthor())
                .created(document.getCreated())
                .build();

        DocumentManager.Document savedDocument = documentManager.save(updatedDocument);

        assertEquals(document.getId(), savedDocument.getId());
        assertEquals(updatedDocument.getTitle(), savedDocument.getTitle());
        assertEquals(updatedDocument.getContent(), savedDocument.getContent());
        assertEquals(updatedDocument.getAuthor(), savedDocument.getAuthor());
        assertEquals(updatedDocument.getCreated(), savedDocument.getCreated());
    }

    @Test
    public void testFindById() {
        DocumentManager.Document document = DocumentManager.Document.builder()
                .id(UUID.randomUUID().toString())
                .title("Test Title")
                .content("Test Content")
                .author(new DocumentManager.Author(UUID.randomUUID().toString(), "Author Name"))
                .created(Instant.now())
                .build();

        documentManager.save(document);

        Optional<DocumentManager.Document> foundDocument = documentManager.findById(document.getId());

        assertTrue(foundDocument.isPresent());
        assertEquals(document.getId(), foundDocument.get().getId());
    }

    @Test
    public void testFindByIdNotFound() {
        Optional<DocumentManager.Document> foundDocument = documentManager.findById(UUID.randomUUID().toString());

        assertFalse(foundDocument.isPresent());
    }

    @Test
    public void testSearch() {
        DocumentManager.Document document1 = DocumentManager.Document.builder()
                .id(UUID.randomUUID().toString())
                .title("Test Title 1")
                .content("Test Content 1")
                .author(new DocumentManager.Author(UUID.randomUUID().toString(), "Author 1"))
                .created(Instant.now())
                .build();

        DocumentManager.Document document2 = DocumentManager.Document.builder()
                .id(UUID.randomUUID().toString())
                .title("Test Title 2")
                .content("Test Content 2")
                .author(new DocumentManager.Author(UUID.randomUUID().toString(), "Author 2"))
                .created(Instant.now())
                .build();

        documentManager.save(document1);
        documentManager.save(document2);

        DocumentManager.SearchRequest request = DocumentManager.SearchRequest.builder()
                .titlePrefixes(List.of("Test Title"))
                .build();

        List<DocumentManager.Document> results = documentManager.search(request);

        assertEquals(2, results.size());
    }
}