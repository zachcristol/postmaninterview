# Postman Onsite Interview

## TODO before Monday 9/14

- [ ] **Pre-assessment** - DB + backend + docker-compose (no UI/frontend yet)
- [ ] **Reply to Bernard** confirming you received the prep guide email and PDF attachment
- [ ] **Sign NDA** - was promised in a separate email, hasn't arrived yet - follow up with Isabella (isabella.antikoll@postman.com) if it doesn't show
- [ ] **System design prep** - brush up on distributed systems architecture (explicitly called out as where candidates struggle)

---


**Role:** Senior Fullstack Engineer - Codegen
**Date:** Monday, September 14, 2026
**Location:** WeWork - 10900 Stonelake Blvd, Building 2, Suite 100, Austin, TX 78759
- Arrive 15 minutes early, check in at front desk and give the receptionist your interviewer's name
- Parking is available out front; nearest transit stop is Braker/MoPac (~7 min walk)

**NDA:** Must be signed before arrival (sent separately).

---

## Schedule

| Time (CT) | Round | Interviewer |
|---|---|---|
| 11:30am - 12:30pm | System Design | Arie Litovsky |
| 12:30pm - 12:45pm | Break | - |
| 12:45pm - 1:45pm | Live Coding | Eduardo Vega |
| 1:45pm - 2:00pm | Break | - |
| 2:00pm - 3:00pm | Behavioral / Cultural Fit | Rudrasen Sitoleh |

---

## Round 1 - System Design (Arie Litovsky)

- 5 min intro/icebreaker, then open-ended problem prompt
- Build out the system design covering functional, low-level, and high-level considerations
- Constraints are added as the conversation progresses - your design must remain feasible
- Can whiteboard on-site or use a virtual tool and share screen (draw.io, whimsical.com, excalidraw.io, witeboard.com)

**What they assess:**
- Asking good clarification questions before diving in
- Clearly stating intentions and requirements up front
- Driving the conversation and articulating trade-offs
- Explaining how system components interact
- Completeness, depth, and adaptability to new requirements

**Split:** 50% high-level design / 50% detailed design + deep dives into components

**Note:** Senior engineers are expected to serve as system architects, not just coders. Distributed systems orchestration/architecture is where candidates most often struggle.

---

## Round 2 - Live Coding (Eduardo Vega)

### Pre-assessment (take-home, bring completed on your laptop)

The theme is **Planet Express** - a cargo delivery company moving goods between planets for customers, stakeholders, and regulators. All that generates data that needs to live somewhere and be reachable by a service.

**Deliverables:**
- Relational database (Postgres, MySQL, SQLite - your choice)
- Backend service in a language/framework you know well, connected to that DB
- Proof the two are talking: a health check endpoint or a simple query through the service
- `docker-compose.yml` (or equivalent) that brings both up with a single command
- Short README on how to verify the connection (command to run, endpoint to hit)

**Wire up your usual boilerplate now** - ORM/query builder, logging, config/env handling, migrations. This is the time for scaffolding, not during the live session.

**Do NOT build yet:** no UI, no CLI, no SDK, no client-facing anything. That's for the live portion.

**Key callout:** Keep it lightweight. If you're deep in schema design or business logic before they've given you the data, that's a sign to pull back. Proving the connection is the goal.

**PDF:** `SDK Pre-Assessment.pdf` is saved in this folder.

### Live portion

- They'll give you data to load and ask you to build a user- or agent-facing application of your choice
- They care more about **how you approach the problem** than a polished result
- Start by working with your preferred AI agent to **create a design or implementation doc** - they evaluate how you interact with AI and assess the plan
- Then have the AI make code changes while they observe how you manage and review its output
- Use any AI tools/workflows you want - having 1,000+ skills ready is a positive signal

**Bring:** Your laptop with the completed pre-assessment take-home ready to go.

---

## Round 3 - Behavioral / Cultural Fit (Rudrasen Sitoleh)

Hiring manager round covering background, experience, ownership, and some technical questions.

**Topics:**
- SDK-related technical questions
- API familiarity and experience in the space
- AI tooling adoption and your overall perspective on it
- Collaborativeness, motivations, and alignment with Postman's mission

**Likely questions:**
1. How you tackle hard and complex problems
2. Experience working with PM, UX, and Eng as a unit
3. Walk through a project you owned - what were the challenges and how did you overcome them (with a positive outcome)
4. Working cross-functionally (XFN) - setting expectations, operating in fast-paced and ambiguous environments

---

## Contacts

| Name | Role | Email |
|---|---|---|
| Bernard Boulos | Staff Technical Recruiter | bernard.boulos@postman.com |
| Isabella Antikoll | Coordinator | isabella.antikoll@postman.com |
