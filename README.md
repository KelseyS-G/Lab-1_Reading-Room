# The Reading Room: Lending Tracker (rebuilt)

A rebuild of the volunteer-written lending tracker. It behaves like the original,
except for the one rule the business says must never be broken:

> An item that is on loan can't be lent again until it's returned.


## Design notes (key decisions)

**1. The item owns the rule, not the library.** 
I could have added `if (item.status.equals("ON_LOAN")) return;` to `Library.borrow`. 
That would fix the bug, but any other code could still skip it, and the fields would still be 
public, so anyone could set the status directly. The rule has to live in the item itself. Instead 
`LendingItem` keeps its state private, and the only way to change it is `lendTo`/`receiveBack`, 
which are package-private. Code outside `domain` physically cannot compile a call that bypasses
the rule. `Library.borrow` calls `item.lendTo(...)` before creating the loan or touching
history, so a refused request changes nothing, and does not even use up a loan number.

**2. One source of truth for status.** 
The original stored `status` and `currentBorrower`, which could disagree. Now only `borrower` is 
stored and `status()` is computed from it. 

**3. Inheritance for item types** 
*Why:* `BookItem` and `AudioKitItem` are lending items that share the same data (the borrower)
 and the same rule (an item can only be lent when it’s available, and returned when it’s out). 
 They differ only in loan length. I chose inheritance because of that shared rule.

**4. Modern Java where it helps:**
- **Enums instead of strings.** `MemberType`, `ItemStatus` and `Loan.Status` replace the old text 
  values, so a typo like `"Studnet"` is a compile error instead of a silent bug. Item types are classes
  for the same reason: the typo `"Book"` that used to create a 0-day loan can no longer be written.
- **Enums carry behaviour.** `MemberType.loanDaysFor()` holds the student rule once, instead of it being
  copied into each branch.
- **`Optional` instead of null.** `borrower()` returns an `Optional`, so a caller has to handle "nobody 
  has it" instead of hitting a surprise null.
- **`var` and `toList()`.** They keep the code short where the type is obvious, and `toList()` returns a read-only list.
- **An unchecked `LendingException`.** It's for a broken rule, not a recoverable problem. The domain 
  throws it, and the `app` package catches it and prints `Refused: ...`.


## Improvement: "What does this member have out?"

I added `Library.activeLoansFor(member)`, which returns the loans a member currently has.

**Why it matters:** at the desk, the most common question is "what does Kofi have?".
In the original the only way to answer it was to scan the whole end-of-day report, or
to take the live loan list from `getLoans()` and filter it, which is the leak from the
diagnosis. Now the answer is one safe call. It returns a read-only snapshot, so asking
the question can't change any records.

