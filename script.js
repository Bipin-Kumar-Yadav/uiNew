document.addEventListener("DOMContentLoaded", function () {
	// Common elements and variables
	const loginForm = document.getElementById("loginForm");
	const registerForm = document.getElementById("registerForm");
	const errorMessage = document.getElementById("errorMessage");
	const navMenu = document.getElementById("navMenu");
	const usernameElement = document.getElementById("username");
	const bookingForm = document.getElementById("bookingForm");
	const bookingOfficerForm = document.getElementById("bookingOfficerForm");
	// Authentication and redirection logic
	function getCurrentPage() {
		const path = window.location.pathname;
		return path.split("/").pop() || "index.html"; // Handle root ("/") as index.html
	}

	// Auth check
	const currentUser = JSON.parse(localStorage.getItem("currentUser"));
	if (!currentUser) {
		const allowedPages = ["index.html", "register.html"];
		const currentPage = getCurrentPage();

		if (!allowedPages.includes(currentPage)) {
			window.location.href = "index.html";
			return;
		}
	}

	if (usernameElement && currentUser) {
		usernameElement.textContent = currentUser.customerName;
	}

	// Navigation menu setup
	if (navMenu && currentUser) {
		const menuItems =
			currentUser.role === "customer"
				? [
						{ text: "Home", link: "dashboard.html" },
						{ text: "Booking Service", link: "booking.html" },
						{ text: "Tracking", link: "tracking.html" },
						{ text: "Previous Booking", link: "previous-booking.html" },
						{ text: "Contact Support", link: "contact-support.html" },
						{ text: "Logout", link: "#", id: "logout" },
				  ]
				: [
						{ text: "Home", link: "dashboard.html" },
						{ text: "Tracking", link: "tracking.html" },
						{ text: "Booking Service", link: "booking-officer.html" },
						{ text: "Delivery Status", link: "delivery-status.html" },
						{ text: "Pickup Scheduling", link: "pickup-scheduling.html" },
						{ text: "Previous Booking", link: "previous-booking-officer.html" },
						{ text: "Logout", link: "#", id: "logout" },
				  ];

		navMenu.innerHTML = menuItems
			.map(
				(item) => `
            <li><a href="${item.link}" ${
					item.link === window.location.pathname ? 'class="active"' : ""
				} 
                ${item.id ? `id="${item.id}"` : ""}>${item.text}</a></li>
        `
			)
			.join("");

		const logoutButton = document.getElementById("logout");
		if (logoutButton) {
			logoutButton.addEventListener("click", (e) => {
				e.preventDefault();
				localStorage.removeItem("currentUser");
				window.location.href = "index.html";
			});
		}
	}

	// Login functionality
	if (loginForm) {
		loginForm.addEventListener("submit", function (e) {
			e.preventDefault();
			const userId = document.getElementById("userId").value;
			const password = document.getElementById("password").value;
			const role = document.getElementById("role").value;
			const users = JSON.parse(localStorage.getItem("users")) || [];
			const user = users.find(
				(u) => u.userId === userId && u.password === password && u.role === role
			);

			if (user) {
				localStorage.setItem("currentUser", JSON.stringify(user));
				window.location.href = "dashboard.html";
			} else {
				errorMessage.textContent = "Invalid User ID, Password, or Role";
			}
		});
	}

	// Registration functionality
	if (registerForm) {
		registerForm.addEventListener("submit", function (e) {
			e.preventDefault();

			const errorMessage = document.getElementById("errorMessage");
			errorMessage.textContent = "";

			const formData = {
				customerName: document.getElementById("customerName").value.trim(),
				email: document.getElementById("email").value.trim(),
				countryCode: document.getElementById("countryCode").value,
				mobile: document.getElementById("mobile").value.trim(),
				address: document.getElementById("address").value.trim(),
				userId: document.getElementById("userId").value.trim(),
				password: document.getElementById("password").value.trim(),
				confirmPassword: document
					.getElementById("confirmPassword")
					.value.trim(),
				role: document.getElementById("role").value,
			};

			let isValid = true;
			let errors = [];
			// Customer Name Validation (Max length 50)
			if (
				formData.customerName.length === 0 ||
				formData.customerName.length > 50
			) {
				errors.push("Customer Name must be between 1 and 50 characters.");
				isValid = false;
			}
			// Email Validation
			const emailPattern = /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/;
			if (!emailPattern.test(formData.email)) {
				errors.push("Enter a valid email address.");
				isValid = false;
			}
			// Mobile Number Validation (10 digits)
			const mobilePattern = /^\d{10}$/;
			if (!mobilePattern.test(formData.mobile)) {
				errors.push("Enter a valid 10-digit mobile number.");
				isValid = false;
			}
			// User ID Validation (5 to 20 characters)
			if (formData.userId.length < 5 || formData.userId.length > 20) {
				errors.push("User ID must be between 5 and 20 characters.");
				isValid = false;
			}
			// Password Validation (8-30 chars, 1 uppercase, 1 lowercase, 1 number, 1 special char)
			const passwordPattern =
				/^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[@$!%*?&])[A-Za-z\d@$!%*?&]{8,30}$/;
			if (!passwordPattern.test(formData.password)) {
				errors.push(
					"Password must be 8-30 characters long, include at least one uppercase letter, one lowercase letter, one number, and one special character."
				);
				isValid = false;
			}

			// Confirm Password Validation
			if (formData.password !== formData.confirmPassword) {
				errors.push("Passwords do not match.");
				isValid = false;
			}

			if (!isValid) {
				errorMessage.textContent = errors.join("\n");
				errorMessage.style.color = "red";
				return;
			}
			// Check if User ID already exists in localStorage
			const users = JSON.parse(localStorage.getItem("users")) || [];
			if (users.some((u) => u.userId === formData.userId)) {
				errorMessage.textContent = "User ID already exists.";
				errorMessage.style.color = "red";
				return;
			}
			// Save user data
			users.push({
				...formData,
				mobile: `${formData.countryCode} ${formData.mobile}`,
			});
			localStorage.setItem("users", JSON.stringify(users));
			alert("Registration successful!");
			window.location.href = "index.html";
		});
	}

	if (bookingForm) {
		bookingForm.addEventListener("submit", (e) => {
			e.preventDefault();
			const generateBookingId = () =>
				"BOOK-" + Math.random().toString(36).substr(2, 9).toUpperCase();
			let bookingFormData = {
				sender: {
					userId: currentUser.userId,
				},
				bookingId: generateBookingId(),
				receiver: {
					name: document.getElementById("receiverName").value,
					address: document.getElementById("receiverAddress").value,
					pinCode: document.getElementById("receiverPin").value,
					contact: document.getElementById("receiverContact").value,
				},
				parcel: {
					size: document.getElementById("parcelSize").value,
					contents: document.getElementById("parcelContents").value,
					status: "Booked",
					date: new Date().toLocaleString(),
				},
				shipping: {
					deliverySpeed: document.getElementById("deliverySpeed").value,
					packaging: document.getElementById("packaging").value,
				},
				pickup: {
					preferredTime: formatDateTime(
						document.getElementById("pickupTime").value
					),
					dropOffTime: formatDateTime(
						document.getElementById("dropOffTime").value
					),
				},
				payment: {
					cost: document.getElementById("serviceCost").value,
				},
			};
			localStorage.setItem("currentBooking", JSON.stringify(bookingFormData));
			window.location.href = "payment.html";
		});
	}

	if (bookingOfficerForm) {
		bookingOfficerForm.addEventListener("submit", (e) => {
			e.preventDefault();
			let users = JSON.parse(localStorage.getItem("users")) || [];
			const user = users.find(
				(u) => u.customerName === document.getElementById("sender_Name").value
			);
			const user_Id = user ? user.userId : null;
			if (!user_Id) {
				alert("User not found. Please check the sender's name.");
				return;
			}
			const generateBookingId = () =>
				"BOOK-" + Math.random().toString(36).substr(2, 9).toUpperCase();
			let bookingFormData = {
				sender: {
					userId: user_Id,
				},
				bookingId: generateBookingId(),
				receiver: {
					name: document.getElementById("receiverName").value,
					address: document.getElementById("receiverAddress").value,
					pinCode: document.getElementById("receiverPin").value,
					contact: document.getElementById("receiverContact").value,
				},
				parcel: {
					size: document.getElementById("parcelSize").value,
					contents: document.getElementById("parcelContents").value,
				},
				shipping: {
					deliverySpeed: document.getElementById("deliverySpeed").value,
					packaging: document.getElementById("packaging").value,
				},
				pickup: {
					preferredTime: formatDateTime(
						document.getElementById("pickupTime").value
					),
					dropOffTime: formatDateTime(
						document.getElementById("dropOffTime").value
					),
				},
				payment: {
					cost: document.getElementById("serviceCost").value,
				},
			};
			localStorage.setItem("currentBooking", JSON.stringify(bookingFormData));
			window.location.href = "payment.html";
		});
	}
	// Page-specific functionality
	const currentPage = window.location.pathname;
	// Booking Service Page
	if (currentPage === "/booking.html") {
		if (currentUser.role === "customer") {
			document.getElementById("senderName").value = currentUser.customerName;
			document.getElementById("senderAddress").value = currentUser.address;
			document.getElementById("senderContact").value = currentUser.mobile;
		}
		const deliverySpeed = document.getElementById("deliverySpeed");
		const packaging = document.getElementById("packaging");
		const serviceCost = document.getElementById("serviceCost");

		const updateCost = () => {
			const speedCost = deliverySpeed.value === "express" ? 500 : 200;
			const packagingCost = packaging.value === "custom" ? 300 : 100;
			serviceCost.value = `₹${speedCost + packagingCost}`;
		};

		deliverySpeed.addEventListener("change", updateCost);
		packaging.addEventListener("change", updateCost);
		updateCost();
	}

	// Booking Officer Page
	if (currentPage === "/booking-officer.html") {
		const deliverySpeed = document.getElementById("deliverySpeed");
		const packaging = document.getElementById("packaging");
		const serviceCost = document.getElementById("serviceCost");

		const updateCost = () => {
			const speedCost = deliverySpeed.value === "express" ? 500 : 200;
			const packagingCost = packaging.value === "custom" ? 300 : 100;
			serviceCost.value = `₹${speedCost + packagingCost}`;
		};

		deliverySpeed.addEventListener("change", updateCost);
		packaging.addEventListener("change", updateCost);
		updateCost();
	}
	// Payment Page
	if (currentPage === "/payment.html") {
		document.getElementById("paymentSection").classList.remove("hidden");
		let bookingData = JSON.parse(localStorage.getItem("currentBooking"));
		document.getElementById("billAmount").value = bookingData.payment.cost;
		bookingData.payment.paymentMode =
			document.getElementById("paymentMode").value;
		document.getElementById("cardType").value = bookingData.payment.paymentMode;
		document.getElementById("cardHolderName").value = JSON.parse(
			localStorage.getItem("currentUser")
		).customerName;
		document.getElementById("paymentForm").addEventListener("submit", (e) => {
			e.preventDefault();
			document.getElementById("paymentSection").classList.add("hidden");
			document.getElementById("cardDetailsSection").classList.remove("hidden");
		});
		document
			.getElementById("cardDetailsForm")
			.addEventListener("submit", (e) => {
				e.preventDefault();
				const allBookings =
					JSON.parse(localStorage.getItem("allBookings")) || [];
				allBookings.push(bookingData);
				localStorage.setItem("allBookings", JSON.stringify(allBookings));
				window.location.href = "invoice.html";
			});
	}

	// Invoice Page
	if (currentPage === "/invoice.html") {
		const booking = JSON.parse(localStorage.getItem("currentBooking"));

		const invoiceDetails = document.getElementById("invoiceDetails");
		if (booking) {
			console.log("booking yyy", booking.bookingId);
			invoiceDetails.innerHTML = `
            <p><strong>Booking Id:</strong> ${booking.bookingId}</p>
                <p><strong>Receiver Name:</strong> ${booking.receiver.name}</p>
                <p><strong>Receiver Address:</strong> ${
									booking.receiver.address
								}</p>
                <p><strong>Receiver Pin:</strong> ${
									booking.receiver.pinCode
								}</p>
                <p><strong>Receiver Mobile:</strong> ${
									booking.receiver.contact
								}</p>
                <p><strong>Parcel Weight:</strong> ${booking.parcel.size}</p>
                <p><strong>Parcel Contents:</strong> ${
									booking.parcel.contents
								}</p>
                <p><strong>Delivery Type:</strong> ${
									booking.shipping.deliverySpeed
								}</p>
                <p><strong>Packing Preference:</strong> ${
									booking.shipping.packaging
								}</p>
                <p><strong>Pickup Time:</strong> ${booking.pickup.preferredTime.toLocaleString()}</p>
               <p><strong>Drop Off Time:</strong> ${booking.pickup.dropOffTime.toLocaleString()}</p>
                <p><strong>Service Cost:</strong> ${booking.payment.cost}</p>
                <p><strong>Payment Time:</strong> ${new Date().toLocaleString()}</p>
            `;
		}
	}

	// Tracking Page
	if (currentPage === "/tracking.html") {
		document.getElementById("trackingForm").addEventListener("submit", (e) => {
			e.preventDefault();
			const trackingId = document
				.getElementById("trackingId")
				.value.trim()
				.toUpperCase();
			const bookings = JSON.parse(localStorage.getItem("allBookings")) || [];

			// Corrected find() usage
			const booking = bookings.find(
				(b) => b.bookingId.toUpperCase() === trackingId.toUpperCase()
			);
			console.log(booking);
			const resultDiv = document.getElementById("trackingResult");

			if (booking) {
				resultDiv.innerHTML = `
                    <h3>Status: ${booking.parcel.status}</h3>
                    ${
											booking.lastUpdated
												? `<p>Last Updated: ${new Date(
														booking.lastUpdated
												  ).toLocaleString()}</p>`
												: ""
										}
                `;
			} else {
				resultDiv.innerHTML =
					'<p class="error">No booking found with this ID</p>';
			}
		});
	}

	// Previous Bookings Page
	if (currentPage === "/previous-booking.html") {
		const bookingsList = document.getElementById("bookingsList");

		const displayBookings = () => {
			const bookings = JSON.parse(localStorage.getItem("allBookings")) || [];

			const userBookings = bookings.filter(
				(b) => b.sender.userId === currentUser.userId
			);

			bookingsList.innerHTML = userBookings
				.map(
					(booking) => `
                    <div class="booking-item">
						<p>Customer ID: ${booking.sender.userId}</p>
                        <p>Booking ID: ${booking.bookingId}</p>
                        <p>Date: ${booking.parcel.date}</p>
						<p>Receiver Name: ${booking.receiver.name}</p>
						<p>Delivered Address: ${booking.receiver.address}</p>
						<p>Amount: ${booking.payment.cost}</p>
                        <p>Status: ${booking.parcel.status}</p>
                    </div>
                `
				)
				.join("");
		};

		displayBookings();
	}
	// Previous Bookings Page Officer
	if (currentPage === "/previous-booking-officer.html") {
		previousBookingOfficerForm.addEventListener("submit", (e) => {
			e.preventDefault();
			const enteredUserId = document.getElementById("userId").value.trim();

			const bookingsList = document.getElementById("bookingsList");

			const displayBookings = () => {
				const bookings = JSON.parse(localStorage.getItem("allBookings")) || [];

				const userBookings = bookings.filter(
					(b) => b.sender.userId === enteredUserId
				);

				bookingsList.innerHTML = userBookings
					.map(
						(booking) => `
                    <div class="booking-item">
						<p>Customer ID: ${booking.sender.userId}</p>
                        <p>Booking ID: ${booking.bookingId}</p>
                        <p>Date: ${booking.parcel.date}</p>
						<p>Receiver Name: ${booking.receiver.name}</p>
						<p>Delivered Address: ${booking.receiver.address}</p>
						<p>Amount: ${booking.payment.cost}</p>
                        <p>Status: ${booking.parcel.status}</p>
                    </div>
                `
					)
					.join("");
			};

			displayBookings();
		});
	}
	// Update Delivery Status Pages
	if (currentUser?.role === "officer") {
		// Delivery Status Update
		if (currentPage === "/delivery-status.html") {
			document.getElementById("statusForm").addEventListener("submit", (e) => {
				e.preventDefault();
				const bookingId = document
					.getElementById("statusBookingId")
					.value.trim();
				const newStatus = document.getElementById("newStatus").value.trim();
				const allBookings =
					JSON.parse(localStorage.getItem("allBookings")) || [];

				let bookingIndex = allBookings.findIndex(
					(b) => b.bookingId === bookingId
				);

				if (bookingIndex !== -1) {
					allBookings[bookingIndex].parcel.status = newStatus;
					localStorage.setItem("allBookings", JSON.stringify(allBookings));
					alert("Status updated successfully!");
				} else {
					alert("Booking not found!");
				}
			});
		}

		// Pickup Scheduling
		if (currentPage === "/pickup-scheduling.html") {
			document.getElementById("pickupForm").addEventListener("submit", (e) => {
				e.preventDefault();
				const bookingId = document
					.getElementById("pickupBookingId")
					.value.trim();
				const pickupTime = formatDateTime(
					document.getElementById("pickupDateTime").value.trim()
				);
				let allBookings = JSON.parse(localStorage.getItem("allBookings")) || [];

				let bookingIndex = allBookings.findIndex(
					(b) => b.bookingId === bookingId
				);

				if (bookingIndex !== -1) {
					allBookings[bookingIndex].pickup.preferredTime = pickupTime;
					localStorage.setItem("allBookings", JSON.stringify(allBookings));
					alert("Pickup scheduled successfully!");
				} else {
					alert("Booking not found!");
				}
			});
		}
	}
});

function formatDateTime(inputDate) {
	const date = new Date(inputDate);
	const day = String(date.getDate()).padStart(2, "0");
	const month = String(date.getMonth() + 1).padStart(2, "0");
	const year = date.getFullYear();
	const hours = String(date.getHours()).padStart(2, "0");
	const minutes = String(date.getMinutes()).padStart(2, "0");
	const seconds = String(date.getSeconds()).padStart(2, "0");
	return `${day}/${month}/${year} ${hours}:${minutes}:${seconds}`;
}
