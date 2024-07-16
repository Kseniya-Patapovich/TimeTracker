package com.timetracker.controller;

import com.timetracker.model.Record;
import com.timetracker.model.dto.RecordDto;
import com.timetracker.service.RecordService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/records")
@RequiredArgsConstructor
public class RecordController {
    private final RecordService recordService;

    @GetMapping
    public List<Record> getAllRecords() {
        return recordService.getAllRecord();
    }

    @GetMapping("/{id}")
    public Record getRecordBuId(@PathVariable Long id) {
        return recordService.getRecordById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Record with id=" + id + " not found!"));
    }

    @GetMapping("/project/{id}")
    public List<Record> getRecordsByProjectId(@PathVariable("id") Long id) {
        return recordService.getRecordsByProjectId(id);
    }

    @GetMapping("/user/{id}")
    @ResponseStatus
    public List<Record> getRecordByUserId(@PathVariable("id") Long id) {
        return recordService.getRecordByUserId(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Long createRecord(@RequestBody RecordDto recordCreateDto) {
        return recordService.createRecord(recordCreateDto);
    }

    @PutMapping()
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void logTime(@RequestBody RecordDto recordDto) {
        recordService.logTime(recordDto);
    }
}
