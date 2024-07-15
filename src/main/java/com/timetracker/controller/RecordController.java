package com.timetracker.controller;

import com.timetracker.model.Record;
import com.timetracker.model.dto.RecordCreateDto;
import com.timetracker.model.dto.RecordUpdateDto;
import com.timetracker.service.RecordService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/records")
@RequiredArgsConstructor
public class RecordController {
    private final RecordService recordService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<Record> getAllRecords() {
        return recordService.getAllRecord();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Record> getRecordBuId(@PathVariable Long id) {
        Optional<Record> recordFromDb = recordService.getRecordById(id);
        return recordFromDb.map(record -> new ResponseEntity<>(record, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping("/project")
    @ResponseStatus(HttpStatus.OK)
    public List<Record> getRecordsByProjectId(@RequestParam("id") Long id) {
        return recordService.getRecordsByProjectId(id);
    }

    @GetMapping("/user")
    @ResponseStatus(HttpStatus.OK)
    public List<Record> getRecordByUserId(@RequestParam("id") Long id) {
        return recordService.getRecordByUserId(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Long createRecord(@RequestBody RecordCreateDto recordCreateDto) {
        return recordService.createRecord(recordCreateDto);
    }

    @PutMapping()
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void logTime(@RequestParam("userId") Long userId,
                             @RequestParam("projectId") Long projectId,
                             @RequestBody RecordUpdateDto recordUpdateDto) {
        recordService.logTime(userId, projectId, recordUpdateDto);
    }
}
