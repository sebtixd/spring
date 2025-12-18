package com.library.common.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoanEvent {
    private Long loanId;
    private String userEmail;
    private String bookId;
}