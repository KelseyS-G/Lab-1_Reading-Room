# Diagnosis: The Reading Room lending tracker

## What I saw
```
Lent "Things Fall Apart" to Kofi Mensah for 14 days
Lent "French Audio Course" to Ama Owusu for 14 days
Returned "French Audio Course"
Lent "Things Fall Apart" to Ama Owusu for 28 days      <-- This should be impossible

=== The Reading Room - end of day ===
Items:
  "Things Fall Apart" (BOOK): ON_LOAN - with Ama Owusu       <-- Kofi has disappeared
  "French Audio Course" (AUDIO_KIT): AVAILABLE
Active loans:
  Loan #1: "Things Fall Apart" -> Kofi Mensah                <-- two loans,
  Loan #3: "Things Fall Apart" -> Ama Owusu                      but only one physical book
```

One book went out to two people. The report now says Ama has it, the loans say
both do, and nothing complained.

**The rule that must never be broken: an item on loan can't be lent again until it comes back.**

## Defects

| # | What is wrong | Where (class / method) | Root cause | Business risk | Severity |
|---|---------------|------------------------|------------|---------------|----------|
| 1 | An item on loan can be lent again | `Library.borrow` | The item doesn't enforce its own lend rule | Two borrowers, one copy; the real holder is overwritten | **Critical** |
| 2 | Every field is public | `LendingItem`, `Member`, `Loan`, `Library` | No encapsulation | Anyone can mark a borrowed book "available" | **Critical** |
| 3 | The live loan list is handed out | `Library.getLoans` | A leaked mutable reference | A caller can wipe or edit the loan records | **Critical** |
| 4 | Item state is stored twice; loans match items by title | `LendingItem` fields; `Library.returnItem` | Duplicate facts; identity kept as text | Fields disagree; same-title copies close each other's loans | **High** |
| 5 | Item types are hard-coded; student rule is copied | `Library.borrow` | Type behaviour lives in the coordinator | A new type means editing `borrow`; a typo creates a 0-day loan | **High** |
| 6 | Nothing is validated; the domain prints | Constructors, `Library.returnItem`, `Library` (`println`) | Invalid objects allowed; logic mixed with display | Bad data accepted; rules can't be tested | **Medium** |

## Most dangerous: #1

The others defects make the code hard to change. However, this one makes the data wrong in the
one thing a library cannot get wrong: who has which book. Also, It looks normal
in the report, and it destroys information, so report cannot be fixed afterwards. Messy
code costs volunteers time; a wrong loan record costs the library its books. 


## Planned structure

- **`LendingItem`** (abstract) owns the rule. It keeps one private `borrower`, and
  the status comes from that. `BookItem` and `AudioKitItem` only supply their loan period.
- **`MemberType`** (enum) holds the student rule once.
- **`Member`** checks its data on creation and shows its history read-only.
- **`Loan`** points at the real member and item, not their names.
- **`Library`** coordinates, hands out copies, and never prints.
- **`app` package** does the printing and turns a refusal into `Refused: ...`.



