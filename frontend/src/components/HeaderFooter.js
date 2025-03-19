import React from "react";
import "./HeaderFooter.css";

function HeaderFooter() {
    return (
        <div className="header-footer">
            {/* Header Section */}
            <header className="header">
                <div className="logo">
                    <img
                        src="https://www.ncl.ac.uk/mediav8/brand-hub/images/logo%20575px%20brand%20hub%20assets.jpg"
                        alt="University Logo"
                        className="logo-img"
                    />
                </div>
                <h1>The Newcastle University's Bookstore</h1>
            </header>

            {/* Footer Section */}
            <footer className="footer">
                <p></p>
            </footer>
        </div>
    );
}

export default HeaderFooter;
