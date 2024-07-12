package com.timetracker.controller;

import com.timetracker.model.Record;
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
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/record")
@RequiredArgsConstructor
public class RecordController {
    private final RecordService recordService;

    @PostMapping("/start/{userId}/{projectId}")
    public ResponseEntity<HttpStatus> start(@PathVariable("userId") Long userId, @PathVariable("projectId") Long projectId) {
        return new ResponseEntity<>(recordService.startTracking(userId, projectId) ? HttpStatus.CREATED : HttpStatus.CONFLICT);
    }

    @PutMapping("/stop/{id}")
    public ResponseEntity<HttpStatus> stop(@PathVariable("id") Long id) {
        return new ResponseEntity<>(recordService.stopTracking(id) ? HttpStatus.NO_CONTENT : HttpStatus.CONFLICT);
    }

    @GetMapping("/totalTime/{id}")
    public ResponseEntity<Double> getTotalTime(@PathVariable("id") Long id) {
        return new ResponseEntity<>(recordService.getTotalTime(id), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<List<Record>> getRecordByUserId(@PathVariable("id") Long id) {
        return new ResponseEntity<>(recordService.getRecordByUserId(id), HttpStatus.OK);
    }
}
